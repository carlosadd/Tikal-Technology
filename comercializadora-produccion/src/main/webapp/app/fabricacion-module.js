'use strict';

var fabricacionModule = angular.module('tikal.modules.Fabricacion', ['ngRoute','ngResource','angular-ladda']);

fabricacionModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/fabricacion', {templateUrl: 'view/fabricacion/pedido-list.html', controller: 'tikal.modules.Fabricacion.PedidoListCtrl'});
	$routeProvider.when('/fabricacion/:pedidoId', {templateUrl: 'view/fabricacion/pedido-detail.html', controller: 'tikal.modules.Fabricacion.PedidoDetailCtrl'});
	$routeProvider.when('/fabricacion/:pedidoId/subPedido/:subPedidoId', {templateUrl: 'view/fabricacion/pedido-detail.html', controller: 'tikal.modules.Fabricacion.subPedido.PedidoDetailCtrl'});
});

fabricacionModule.controller('tikal.modules.Fabricacion.PedidoListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Fabricación';
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPedidos = function() {
		$scope.pedidoDao.query().$promise.then(function(data) {
			$scope.pedidos = data;
			$scope.pedidos.items.sort(function(a,b){
				var dateA = new Date(a.fechaDeCreacion.split('+')[0]);
				var dateB = new Date(b.fechaDeCreacion.split('+')[0]);
				if (dateA.getTime() > dateB.getTime()) {
					return -1;
				}
				if (dateB.getTime() > dateA.getTime()) {
					return 1;
				}
				return 0;
			});
		}, function(errResponse) {

		});
	};
	$scope.loadPedidos();
	$scope.verPedido = function(pedidoId) {
	  $location.path($location.path() + '/' + pedidoId);
	};
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
	'$uibModal', 'contactoRaizDao', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $uibModal, contactoRaizDao, jsonUtil) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Fabricación',
		link: '/fabricacion/'
	});
	$scope.currentPedidoId = $routeParams.pedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
		//$scope.$broadcast('pedidoCargado', $scope.pedido);
		//$scope.$emit('pedidoCargado', $scope.pedido);
		$scope.navegacion.actual.display = $scope.pedido.nombre;
	}, function(errResponse) {
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.subPedido.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
	'$uibModal', 'contactoRaizDao', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $uibModal, contactoRaizDao, jsonUtil) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	};
	$scope.navegacion.previos.push({
		display: 'Fabricacion',
		link: '/fabricacion/'
	});
	$scope.pedidoRaizId = $routeParams.pedidoId;
	$scope.currentPedidoId = $routeParams.subPedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId, subPedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao.get({pedidoId:$routeParams.pedidoId}).$promise.then(function (data) {
		$scope.navegacion.previos.push({
			display: data.nombre,
			link: '/fabricacion/' + data.id
		});
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
		//$scope.$broadcast('pedidoCargado', $scope.pedido);
		//$scope.$emit('pedidoCargado', $scope.pedido);
		$scope.navegacion.actual.display = $scope.pedido.puntoEntrega.nombreCorto;
	}, function(errResponse) {
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.consultaCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento',{pedidoId:$scope.currentPedidoId}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPedimentos = function() {
		$scope.pedimentoDao.query().$promise.then(function(data) {
			$scope.pedido.pedimentos = data.items;
		}, function(errResponse) {

		});
	};
	$scope.$on('nuevoPedimento', function(event, data) {
		$scope.loadPedimentos();
	});
	$scope.$on('borrarPedimento', function(event, data) {
		$scope.loadPedimentos();
	});
	$scope.$watch('pedido', function () {
		if ($scope.pedido && $scope.pedido.id && $scope.pedido.type != 'PedidoCompuestoIntermediario') {
			$scope.loadPedimentos();
		}
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.consultaAllCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento',{pedidoId:$scope.currentPedidoId}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.pedimentos = [];
	$scope.sumaPedimentos = [];
	var cargaPedimentoCount = 0;
	$scope.loadPedimentos = function(pedido) {
		cargaPedimentoCount = cargaPedimentoCount + 1;
		$scope.pedimentoDao.query({pedidoId:pedido.id}).$promise.then(function(data) {
			pedido.pedimentos = data.items;
			var tmp = [];
			angular.forEach($scope.subPedidos.items, function(pedido, key) {
				tmp.push(pedido.pedimentos);
			});
			$scope.sumaPedimentos = tmp;
			cargaPedimentoCount = cargaPedimentoCount - 1;
			if (cargaPedimentoCount < 1) {
				$scope.pedimentos = $scope.sumaPedimentos;
			}
		}, function(errResponse) {

		});
	};
	$scope.$on('nuevoPedimento', function(event, data) {
		if (data.partida.type == 'GrupoProducto') {
			var tmpQueue = [];
			angular.forEach(data.partida.partidas, function(subPartidas, key) {
				tmpQueue.push(subPartidas.pedido);
			});
			$scope.loadPedimentoQueue = tmpQueue;
		} else {
			if (data.partida) {
				var tmpQueue = [];
				tmpQueue.push(data.partida.pedido);
				$scope.loadPedimentoQueue = tmpQueue;
			} else {
				$scope.pedimentos = [];
				var tmpQueue = [];
				angular.forEach($scope.subPedidos.items, function(pedido, key) {
					tmpQueue.push(pedido);
				});
				$scope.loadPedimentoQueue = tmpQueue;
			}
		}
	});
	$scope.$watch('subPedidos', function(newValue, oldValue) {
		if (newValue && newValue.items) {
			$scope.pedimentos = [];
			var tmpQueue = [];
			angular.forEach(newValue.items, function(pedido, key) {
				tmpQueue.push(pedido);
			});
			$scope.loadPedimentoQueue = tmpQueue;
		}
	});
	$scope.loadPedimentoQueue = [];
	$scope.$watch('loadPedimentoQueue', function(newValue, oldValue) {
		angular.forEach($scope.loadPedimentoQueue, function(pedido, key) {
			$scope.loadPedimentos(pedido);
		});
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.resumen', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $uibModal, $log) {
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento',{pedidoId:$scope.currentPedidoId}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.entradaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/entrada/:entradaId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.workSize = 0;
	$scope.workDone = 0;
	$scope.loadPedimentos = function(pedido) {
		$scope.pedimentoDao.query({pedidoId:pedido.id}).$promise.then(function(data) {
			angular.forEach(data.items, function(pedimento, key) {
				pedimento.pedido = pedido;
			});
			pedido.pedimentos = data.items;
			$scope.workDone = $scope.workDone + 1;
		}, function(errResponse) {

		});
	};
	$scope.loadEntradas = function(pedido) {
		$scope.entradaDao.query({pedidoId:pedido.id}).$promise.then(function(data) {
			pedido.entradas = data.items;
			$scope.workDone = $scope.workDone + 1;
		}, function(errResponse) {

		});
	}
	$scope.loadProveedores = function() {
		contactoRaizDao.query({group:'proveedor'}).$promise.then(function(data) {
			$scope.proveedores = data.items;
			$scope.workDone = $scope.workDone + 1;
		}, function(errResponse) {
		
		});
	}
	$scope.fillNames = function() {
		angular.forEach($scope.subPedidos.items, function(pedido, key) {
			angular.forEach(pedido.pedimentos, function(pedimento, i) {
				angular.forEach($scope.proveedores, function(proveedor, j) {
					if (pedimento.idProveedor == proveedor.id) {
						//pedimento.proveedorName = proveedor.name.name;
						pedimento.proveedor = proveedor;
					}
				});
			});
		});
	}
	$scope.addEntradas = function() {
		angular.forEach($scope.subPedidos.items, function(pedido, key) {
			angular.forEach(pedido.pedimentos, function(pedimento, i) {
				angular.forEach(pedido.entradas, function(entrada, j) {
					if (pedimento.type == 'GrupoPedimento' && entrada.type == 'GrupoRegistroAlmacen') {
						if (pedimento.linea.catalogoId == entrada.linea.catalogoId && pedimento.linea.id == entrada.linea.id && pedimento.idProveedor == entrada.idProveedor) {
							if (!pedimento.entradas) {
								pedimento.entradas = [];
							}
							pedimento.entradas.push(entrada);
						}
					}
					if (pedimento.type == 'PedimentoIntermediario' && entrada.type == 'EntradaIntermediario') {
						if (pedimento.producto.catalogoId == entrada.producto.catalogoId && pedimento.producto.id == entrada.producto.id && pedimento.idProveedor == entrada.idProveedor) {
							if (!pedimento.entradas) {
								pedimento.entradas = [];
							}
							pedimento.entradas.push(entrada);
						}
					}
				});
				var entradaPedimento = 0;
				angular.forEach(pedimento.entradas, function(entrada, i) {
					entradaPedimento = entradaPedimento + entrada.cantidad;
				});
				pedimento.entrada = entradaPedimento;
				pedimento.pendiente = pedimento.cantidad - pedimento.entrada;
			});
		});
	}
	$scope.sortByProducto = function() {
		var productos = [];
		angular.forEach($scope.subPedidos.items, function(pedido, key) {
			angular.forEach(pedido.pedimentos, function(pedimento, i) {
				var existente = false;
				if (pedimento.type == 'GrupoPedimento') {
					angular.forEach(productos, function(producto, j) {
						if (producto.type == 'LineaProductosPorTalla' && producto.catalogoId == pedimento.linea.catalogoId && producto.id == pedimento.linea.id) {
							existente = true;
							producto.pedimentos.push(pedimento);
						}
					});
					if (!existente) {
						pedimento.linea.pedimentos = [];
						pedimento.linea.pedimentos.push(pedimento);
						productos.push(pedimento.linea);
					}
				} else {
					angular.forEach(productos, function(producto, j) {
						if (producto.type == 'ProductoIntermediario' && producto.catalogoId == pedimento.producto.catalogoId && producto.id == pedimento.producto.id) {
							existente = true;
							producto.pedimentos.push(pedimento);
						}
					});
					if (!existente) {
						pedimento.producto.pedimentos = [];
						pedimento.producto.pedimentos.push(pedimento);
						productos.push(pedimento.producto);
					}
				}
			});
		});
		//organiza por proveedor
		angular.forEach(productos, function(producto, key) {
			var proveedores = [];
			angular.forEach(producto.pedimentos, function(pedimento, i) {
				var encontrado = false;
				angular.forEach(proveedores, function(proveedor, j) {
					if (proveedor.id == pedimento.idProveedor) {
						encontrado = true;
						proveedor.pedimentos.push(pedimento);
					}
				});
				if (!encontrado) {
					var copiaProveedor = {
						type: pedimento.proveedor.type,
						id: pedimento.proveedor.id,
						name: pedimento.proveedor.name,
					};
					copiaProveedor.pedimentos = [];
					copiaProveedor.pedimentos.push(pedimento);
					proveedores.push(copiaProveedor);
				}
			});
			producto.pedimentos = null;
			producto.proveedores = proveedores;
		});
		//calcula totales;
		angular.forEach(productos, function(producto, key) {
			var total = 0;
			var entradaTotal = 0;
			angular.forEach(producto.proveedores, function(proveedor, i) {
				var totalProveedor = 0;
				var totalEntradaProveedor = 0;
				angular.forEach(proveedor.pedimentos, function(pedimento, i) {
					totalProveedor = totalProveedor + pedimento.cantidad;
					totalEntradaProveedor = totalEntradaProveedor + pedimento.entrada;
				});
				proveedor.cantidad = totalProveedor;
				proveedor.entrada = totalEntradaProveedor;
				proveedor.pendiente = proveedor.cantidad - proveedor.entrada;
				total = total + proveedor.cantidad;
				entradaTotal = entradaTotal + proveedor.entrada;
			});
			producto.cantidad = total;
			producto.entrada = entradaTotal;
			producto.pendiente = producto.cantidad - producto.entrada;
		});
		productos.sort(function(a,b){
			return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
		});
		angular.forEach(productos, function(producto, key) {
			producto.proveedores.sort(function(a,b){
				return a.name.name.localeCompare(b.name.name);
			});
			angular.forEach(producto.proveedores, function(proveedores, key) {
				proveedores.pedimentos.sort(function(a,b){
					return a.pedido.puntoEntrega.nombreCorto.localeCompare(b.pedido.puntoEntrega.nombreCorto);
				});
			});
		});
		$scope.productos = productos;
	}
	
	$scope.sortByProveedor = function() {
		var proveedorAsignado = [];
		angular.forEach($scope.subPedidos.items, function(pedido, key) {
			angular.forEach(pedido.pedimentos, function(pedimento, i) {
				var existente = false;
				angular.forEach(proveedorAsignado, function(proveedor, j) {
					if (proveedor.id == pedimento.idProveedor) {
						existente = true;
						proveedor.pedimentos.push(pedimento);
					}
				});
				if (!existente) {
					pedimento.proveedor.pedimentos = [];
					pedimento.proveedor.pedimentos.push(pedimento);
					proveedorAsignado.push(pedimento.proveedor);
				}
			});
		});
		//organiza por producto
		angular.forEach(proveedorAsignado, function(proveedor, key) {
			var productos = [];
			angular.forEach(proveedor.pedimentos, function(pedimento, i) {
				var existente = false;
				angular.forEach(productos, function(producto, j) {
					if (pedimento.type == 'GrupoPedimento' && producto.type == 'LineaProductosPorTalla' 
						&& producto.catalogoId == pedimento.linea.catalogoId && producto.id == pedimento.linea.id) {
							existente = true;
							producto.pedimentos.push(pedimento);
					}
					if (pedimento.type == 'PedimentoIntermediario' && producto.type == 'ProductoIntermediario' 
						&& producto.catalogoId == pedimento.producto.catalogoId && producto.id == pedimento.producto.id) {
						existente = true;
						producto.pedimentos.push(pedimento);
					}
				});
				if (!existente) {
					var copiaProducto;
					if (pedimento.type == 'GrupoPedimento') {
						copiaProducto = {
							type: pedimento.linea.type,
							id: pedimento.linea.id,
							catalogoId: pedimento.linea.catalogoId,
							datosGenerales: pedimento.linea.datosGenerales
						};
					}
					if (pedimento.type == 'PedimentoIntermediario') {
						copiaProducto = {
							type: pedimento.producto.type,
							id: pedimento.producto.id,
							catalogoId: pedimento.producto.catalogoId,
							datosGenerales: pedimento.producto.datosGenerales
						};
					}
					copiaProducto.pedimentos = [];
					copiaProducto.pedimentos.push(pedimento);
					productos.push(copiaProducto);
				}
			});
			proveedor.productos = productos;
			proveedor.pedimentos = [];
		});
		//calcula totales;
		angular.forEach(proveedorAsignado, function(proveedor, key) {
			var total = 0;
			var entradaTotal = 0;
			angular.forEach(proveedor.productos, function(producto, i) {
				var totalProducto = 0;
				var entradaTotalProducto = 0;
				angular.forEach(producto.pedimentos, function(pedimento, j) {
					totalProducto = totalProducto + pedimento.cantidad;
					entradaTotalProducto = entradaTotalProducto + pedimento.entrada;
				});
				producto.cantidad = totalProducto;
				producto.entrada = entradaTotalProducto;
				producto.pendiente = producto.cantidad - producto.entrada;
				total = total + producto.cantidad;
				entradaTotal = entradaTotal + producto.entrada;
			});
			proveedor.cantidad = total;
			proveedor.entrada = entradaTotal;
			proveedor.pendiente = proveedor.cantidad - proveedor.entrada;
		});
		proveedorAsignado.sort(function(a,b){
			return a.name.name.localeCompare(b.name.name);
		});
		angular.forEach(proveedorAsignado, function(proveedor, key) {
			proveedor.productos.sort(function(a,b){
				return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
			});
			angular.forEach(proveedor.productos, function(producto, key) {
				producto.pedimentos.sort(function(a,b){
					return a.pedido.puntoEntrega.nombreCorto.localeCompare(b.pedido.puntoEntrega.nombreCorto);
				});
			});
		});
		$scope.proveedorAsignado = proveedorAsignado;
	}
	
	$scope.$watch('subPedidos', function(newValue, oldValue) {
		if ($scope.subPedidos && $scope.subPedidos.items) {
			$scope.workSize = ($scope.subPedidos.items.length * 2) + 1;
			$scope.loadProveedores();
			angular.forEach($scope.subPedidos.items, function(pedido, key) {
				$scope.loadPedimentos(pedido);
				$scope.loadEntradas(pedido);
			});
		}
	});
	$scope.$watch('workDone', function(newValue, oldValue) {
		$scope.dynamic=Math.floor($scope.workDone/$scope.workSize*100);
		if ($scope.workSize > 0 && $scope.workSize == $scope.workDone) {
			$log.info('phase 2');
			$scope.fillNames();
			$scope.addEntradas();
			$scope.sortByProducto();
			$scope.sortByProveedor();
		}
	});
	//funcion reutilizable
	$scope.hideShowDetail = function(item, items) {
		if (item.showSub) {
			item.showSub = false;
		} else {
			angular.forEach(items, function(value, key) {
				value.showSub = false;
			});
			item.showSub = true;
		}
	};
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.creacionCtrl', ['$scope', '$resource', '$routeParams', 'PedimentoCalculator', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, PedimentoCalculator, config, $location, $uibModal, $log) {
	$scope.grupoContacto = {
		display:'Proveedores',
		id:'proveedor'
	};
    $scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento',{}, {
		'query':  {method:'GET', isArray:false}
	});
	
	$scope.asignacionStatus = function (partida, pedimentos) {
		var cantidad = partida.cantidad - PedimentoCalculator.getTotalPedimento(partida, pedimentos);
		if (cantidad == 0) {
			return 'completa';
		}
		if (cantidad > 0) {
			return 'incompleta';
		}
		if (cantidad < 0) {
			return 'excedente';
		}
		return '';
	};
	
	$scope.asignacionRapida = function (partida, size) {
		var modeloAsignacion = {
			proveedor: null,
			partidaType: partida.type
		};
		if (partida.type == 'PartidaIntermediario') {
			modeloAsignacion.cantidad = partida.cantidad - PedimentoCalculator.getTotalPedimento(partida, partida.pedido.pedimentos);
			modeloAsignacion.max = modeloAsignacion.cantidad;
		}
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/fabricacion/asignacion-rapida.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return modeloAsignacion;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//
		});
		modeloAsignacion.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$modalScope.dynamic=0;
			if (partida.type == 'GrupoProducto') {
				var total = 0;
				$modalScope.workQueue = [];
				angular.forEach(partida.partidas, function(elemento, key) {
					if (elemento.type == 'PartidaIntermediario') {
						total = total + 1;
						$modalScope.workQueue.push(elemento);
					} 
					if (elemento.type == 'GrupoPartida') {
						angular.forEach(elemento.partidas, function(subElemento, key) {
							total = total + 1;
							$modalScope.workQueue.push(subElemento);
						});
					}
				});
			}
			if (partida.type == 'GrupoPartida') {
				var total = 0;
				$modalScope.workQueue = [];
				angular.forEach(partida.partidas, function(subElemento, key) {
					total = total + 1;
					$modalScope.workQueue.push(subElemento);
				});
			}
			if (partida.type == 'PartidaIntermediario') {
				var total = 1;
				$modalScope.workQueue = [];
				$modalScope.workQueue.push(partida);
			}
			$modalScope.$watch('workQueue', function(newValue, oldValue) {
				$modalScope.dynamic=Math.floor((total-newValue.length)/total*100);
				var working = false;
				while (newValue.length > 0 && !working) {
					var cantidad = 0;
					if ($modalScope.modelo.cantidad) {
						cantidad = $modalScope.modelo.cantidad;
					} else {
						cantidad = newValue[0].cantidad - PedimentoCalculator.getTotalPedimento(newValue[0], newValue[0].pedido.pedimentos);
					}
					if (cantidad > 0) {
						working = true;
						var nuevoPedimento = {
							type:'PedimentoIntermediarioTransient',
							cantidad:cantidad,
							producto:{
								type:'ProductoReference',
								catalogoId:newValue[0].producto.catalogoId,
								id:newValue[0].producto.id
							},
							idProveedor:$modalScope.modelo.proveedor.id
						};
						$scope.pedimentoDao.save({pedidoId:newValue[0].pedidoId}, nuevoPedimento).$promise.then(function(data) {
							var tmp = [];
							angular.forEach(newValue, function(elemento, key) {
								if (key != 0) {
									tmp.push(elemento);
								}
							});
							$modalScope.workQueue = tmp;
							if ($modalScope.workQueue.length < 1) {
								$scope.$emit('nuevoPedimento', {
									pedimento:nuevoPedimento,
									partida:partida
								});
							}
						}, function(errResponse) {

						});
					} else {
						newValue.splice(0,1);
					}
				}
				if (!working) {
					modalInstance.close($modalScope.modelo);
				}
			});
		};
	};
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.edicionCtrl', ['$scope', '$resource', '$routeParams', 'PedimentoCalculator', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, PedimentoCalculator, config, $location, $uibModal, $log) {
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento/:pedimentoId',{pedimentoId:'@id'}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.editarPedimento = function (pedimento, parent, size) {
		if (parent) {
			pedimento.producto.datosGenerales = parent.linea.datosGenerales;
		}
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/fabricacion/pedimento-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return pedimento;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			pedimento.cantidad = modelo.cantidad;
			if (parent) {
				var total = 0;
				angular.forEach(parent.pedimentos, function(elemento, key) {
					total = total + elemento.cantidad;
				});
				parent.cantidad = total;
			}
		});
		pedimento.command = function($modalScope) {
			var tmpModel = {
				type:'PedimentoIntermediarioTransient',
				id:$modalScope.modelo.id,
				idProveedor:$modalScope.modelo.idProveedor,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.producto.catalogoId,
					id:$modalScope.modelo.producto.id
				},
				cantidad:$modalScope.modelo.cantidad
			};
			$modalScope.actionLoading = true;
			$scope.pedimentoDao.save({pedidoId:$modalScope.modelo.pedidoId}, tmpModel).$promise.then(function(data) {
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
	$scope.borrarPedimento = function(pedimento, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar la asignación al proveedor?',
			type:'warning'
		}
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/contacto/delete-dialog-modal.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return dialogModel;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//delete $scope.pedido['puntoEntrega'];
			$scope.$emit('borrarPedimento',modelo);
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.pedimentoDao.delete({pedidoId:pedimento.pedidoId, pedimentoId:pedimento.id}).$promise.then(function(data) {
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.fabricante.listCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $uibModal, $log) {
	$scope.entradaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/entrada/:entradaId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.fabricantes = [];
	$scope.procesaPedimentos = function() {
		var datos = [];
		angular.forEach($scope.pedido.pedimentos, function(pedimento, i) {
			var repetido = false;
			angular.forEach(datos, function(fabricante, j) {
				if (fabricante.id == pedimento.idProveedor) {
					repetido = true;
					fabricante.pedimentos.push(pedimento);
				}
			});
			if (!repetido) {
				var tmpFabricante = {
					id:pedimento.idProveedor,
					name:{
						name:''
					},
					pedimentos:[]
				};
				tmpFabricante.pedimentos.push(pedimento);
				datos.push(tmpFabricante);
			}
		});
		$scope.fabricantes = datos;
		if ($scope.fabricantes.length > 0) {
			contactoRaizDao.query({group:'proveedor'}).$promise.then(function(data) {
				var proveedores = data.items;
				angular.forEach($scope.fabricantes, function(fabricante, i) {
					angular.forEach(proveedores, function(proveedor, j) {
						if (proveedor.id == fabricante.id) {
							fabricante.name.name = proveedor.name.name;
						}
					});
				});
				$scope.fabricantes.sort(function(a,b){
					return a.name.name.localeCompare(b.name.name);
				});
			}, function(errResponse) {
			
			});
			$scope.entradaDao.query({pedidoId:$scope.pedido.id}).$promise.then(function(data) {
				angular.forEach($scope.fabricantes, function(fabricante, key) {
					fabricante.entradas = [];
					angular.forEach(data.items, function(entrada, i) {
						if (entrada.idProveedor == fabricante.id) {
							fabricante.entradas.push(entrada);
						}
					});
				});
				//calcula los restantes por pedimento por fabricante
				angular.forEach($scope.fabricantes, function(fabricante, i) {
					angular.forEach(fabricante.pedimentos, function(pedimento, j) {
						$scope.calculaTotalesEntrada(pedimento, fabricante.entradas);
					});
				});
				$scope.$broadcast('updateEntradas');
			}, function(errResponse) {

			});
		}
	};
	$scope.$watch('pedido.pedimentos', function(){
		if ($scope.pedido && $scope.pedido.pedimentos) {
			$scope.procesaPedimentos();
		}
	});
	//repetida mover a otro lado
	$scope.calculaTotalesEntrada = function(pedimento, entradas) {
		if (pedimento.type == 'GrupoPedimento') {
			var total = 0;
			var totalEntrada = 0;
			angular.forEach(pedimento.pedimentos, function(pedimentoGrupo, key) {
				$scope.calculaTotalesEntrada(pedimentoGrupo, entradas);
				total = total + pedimentoGrupo.cantidad;
				totalEntrada = totalEntrada + pedimentoGrupo.totalEntrada;
			});
			pedimento.totalEntrada = totalEntrada;
			pedimento.restante = total - totalEntrada;
		} else {
			//pedimento.restante = pedimento.cantidad;
			pedimento.totalEntrada = 0;
			angular.forEach(entradas, function(entrada, i) {
				if (entrada.type == 'GrupoRegistroAlmacen') {
					angular.forEach(entrada.registros, function(registro, j) {
						if (pedimento.producto.id == registro.producto.id && pedimento.producto.catalogoId == registro.producto.catalogoId) {
							pedimento.totalEntrada = pedimento.totalEntrada + registro.cantidad;
						}
					});
				} else {
					if (pedimento.producto.id == entrada.producto.id && pedimento.producto.catalogoId == entrada.producto.catalogoId) {
						pedimento.totalEntrada = pedimento.totalEntrada + entrada.cantidad;
					}
				}
			});
			pedimento.restante = pedimento.cantidad - pedimento.totalEntrada;
		}
	};
}]);

fabricacionModule.directive('pedimentoTotal', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/pedimento-total.html',
	scope: {
      partida: '=partida',
	  pedimentos: '=pedimentos'
    },
	controller: ['$scope', 'PedimentoCalculator', '$log', function($scope, PedimentoCalculator, $log) {
		$scope.$watch('pedimentos', function(){
			$scope.suma = {
				pedimento:PedimentoCalculator.getTotalPedimento($scope.partida, $scope.pedimentos)
			};
		});
	}]
  };
});

fabricacionModule.directive('detallePendiente', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/detalle-pendiente.html',
	scope: {
      pendiente: '=pendiente',
	  items: '=items',
	  propiedad: '@propiedad'
    },
	controller: ['$scope', '$log', function($scope, $log) {
		var calculaTotal = function() {
			if ($scope.pendiente) {
				$scope.total = $scope.pendiente
			} else {
				var total = 0;
				angular.forEach($scope.items, function(value, key) {
					total = total + value[$scope.propiedad];
				});
				$scope.total = total;
			}
		};
		$scope.$on('updateEntradas', function (event, data){
			calculaTotal();
		});
		$scope.$watch('items', function() {
			calculaTotal();
		});
		$scope.$watch('pendiente', function() {
			calculaTotal();
		});
	}]
  };
});

fabricacionModule.directive('sumatoriaBasica', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/sumatoria-basica.html',
	scope: {
	  items: '=items',
	  propiedad: '@propiedad'
    },
	controller: ['$scope', '$log', function($scope, $log) {
		var calculaTotal = function() {
			var total = 0;
			angular.forEach($scope.items, function(value, key) {
				total = total + value[$scope.propiedad];
			});
			$scope.total = total;
		};
		$scope.$on('updateEntradas', function (event, data){
			calculaTotal();
		});
		$scope.$watch('items', function(){
			calculaTotal();
		});
	}]
  };
});

fabricacionModule.directive('pedimentoList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/pedimento-list.html',
	scope: {
	  pedimentos: '=pedimentos'
    },
	controller: ['$scope', '$resource', 'PedimentoCalculator', 'PRODUCCION_REMOTE_ADDRESS', '$log', function($scope, $resource, PedimentoCalculator, config, $log) {
		$scope.lineaDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:lineaProductoId',{}, {
			'get':  {method:'GET', isArray:false}
		});
		$scope.hideShowSub = function(pedimento) {
			if (pedimento.showSub) {
				pedimento.showSub = false;
			} else {
				angular.forEach($scope.pedimentosOrdenados, function(value, key) {
					if (value.type=='GrupoPedimento') {
						value.showSub = false;
					}
				});
				pedimento.showSub = true;
			}
		};
		$scope.$watch('pedimentos', function(){
			var datos = [];
			angular.forEach($scope.pedimentos, function(pedimento, key) {
				if (pedimento.type == 'GrupoPedimento') {
					pedimento.pedimentos.sort(function(a,b){
						return a.producto.talla.localeCompare(b.producto.talla);
					});
				}
				datos.push(pedimento);
			});
			datos.sort(function(a,b){
				var objA;
				var objB;
				if (a.type == 'PedimentoIntermediario') {
					objA = a.producto.datosGenerales;
				} else {
					objA = a.linea.datosGenerales;
				}
				if (b.type == 'PedimentoIntermediario') {
					objB = b.producto.datosGenerales;
				} else {
					objB = b.linea.datosGenerales;
				}
				return objA.nombre.localeCompare(objB.nombre);
			});
			$scope.pedimentosOrdenados = datos;
		});
	}]
  };
});

fabricacionModule.directive('fabricanteList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/fabricante-list.html'
  };
});

fabricacionModule.directive('subPedidoFabricacionList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/fabricacion/subPedido-list.html'
  };
});

fabricacionModule.factory('PedimentoCalculator', ['$log', function($log) {
	var getTotalPorPartida = function(partida, pedimentos) {
		var total = 0;
		angular.forEach(pedimentos, function(value, key) {
			if (value.type == 'GrupoPedimento') {
				total = total + getTotalPorPartida(partida, value.pedimentos);
			} else {
				if (value.producto.catalogoId == partida.producto.catalogoId && value.producto.id == partida.producto.id) {
					total = total + value.cantidad;
				}
			}
		});
		return total;
	};
	var	getTotalPorLinea = function(partida, pedimentos) {
		var total = 0;
		angular.forEach(pedimentos, function(value, key) {
			if (value.type == 'GrupoPedimento') {
				total = total + getTotalPorLinea(partida, value.pedimentos);
			} else {
				var productoTmp;
				if (partida.linea) {
					productoTmp = partida.linea;
				} else {
					productoTmp = partida.producto;
				}
				if (value.producto.lineaDeProductosId && value.producto.catalogoId == productoTmp.catalogoId && 
					value.producto.lineaDeProductosId == productoTmp.id) {
					total = total + value.cantidad;
				}
			}
		});
		return total;
	};
	var	getTotalPorGrupo = function(partida, pedimentos) {
		var total = 0;
		angular.forEach(pedimentos, function(subPedimento, key) {
			if (partida.producto.type == 'ProductoIntermediario') {
				total = total + getTotalPorPartida(partida, subPedimento);
			}
			if (partida.producto.type == 'LineaProductosPorTalla') {
				total = total + getTotalPorLinea(partida, subPedimento);
			}
		});
		return total;
	};
    return {
		getTotalPedimento: function(partida, pedimentos) {
			if (partida.type == 'PartidaIntermediario') {
				return getTotalPorPartida(partida, pedimentos);
			}
			if (partida.type == 'GrupoPartida') {
				return getTotalPorLinea(partida, pedimentos);
			}
			if (partida.type == 'GrupoProducto') {
				return getTotalPorGrupo(partida, pedimentos);
			}
		}
	};
}]);

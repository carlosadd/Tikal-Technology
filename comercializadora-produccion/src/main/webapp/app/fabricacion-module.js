/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
'use strict';

var fabricacionModule = angular.module('tikal.modules.Fabricacion', ['ngRoute','ngResource','angular-ladda','popoverToggle']);

fabricacionModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/fabricacion', {templateUrl: 'view/fabricacion/pedido-list.html', controller: 'tikal.modules.Fabricacion.PedidoListCtrl'});
	$routeProvider.when('/fabricacion/:pedidoId', {templateUrl: 'view/fabricacion/pedido-detail.html', controller: 'tikal.modules.Fabricacion.PedidoDetailCtrl'});
	$routeProvider.when('/fabricacion/:pedidoId/subPedido/:subPedidoId', {templateUrl: 'view/fabricacion/pedido-detail.html', controller: 'tikal.modules.Fabricacion.subPedido.PedidoDetailCtrl'});
});

fabricacionModule.controller('tikal.modules.Fabricacion.PedidoListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $modal, $log) {
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
	'$modal', 'contactoRaizDao','$window', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $modal, contactoRaizDao, $window, jsonUtil) {
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
		$window.sessionStorage.setItem('nombrePedidoActual', $scope.pedido.nombre);
	}, function(errResponse) {
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.subPedido.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
	'$modal', 'contactoRaizDao','$window', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $modal, contactoRaizDao, $window, jsonUtil) {
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
	$scope.navegacion.previos.push({
		display: $window.sessionStorage.getItem('nombrePedidoActual'),
		link: '/fabricacion/' + $routeParams.pedidoId
	});
	$scope.pedidoRaizId = $routeParams.pedidoId;
	$scope.currentPedidoId = $routeParams.subPedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId, subPedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
		//$scope.$broadcast('pedidoCargado', $scope.pedido);
		//$scope.$emit('pedidoCargado', $scope.pedido);
		$scope.navegacion.actual.display = $scope.pedido.puntoEntrega.nombreCorto;
	}, function(errResponse) {
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.consultaCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $modal, $log) {
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

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.consultaAllCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $modal, $log) {
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
		/*if (newValue.length > 0) {
			$scope.loadPedimentos(newValue[0]);
			var tmpQueue = [];
			angular.forEach(newValue, function(elemento, key) {
				if (key != 0) {
					tmpQueue.push(elemento);
				}
			});
			//mover esto a otra parte si se quiere hacer secuencial o paralelo
			//asi funciona en paralelo
			$scope.loadPedimentoQueue = tmpQueue;
		}*/
	});
}]);

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.creacionCtrl', ['$scope', '$resource', '$routeParams', 'PedimentoCalculator', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, PedimentoCalculator, config, $location, $modal, $log) {
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
		var modalInstance = $modal.open({
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

fabricacionModule.controller('tikal.modules.Fabricacion.Pedimento.edicionCtrl', ['$scope', '$resource', '$routeParams', 'PedimentoCalculator', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, PedimentoCalculator, config, $location, $modal, $log) {
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento/:pedimentoId',{pedimentoId:'@id'}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.editarPedimento = function (pedimento, parent, size) {
		var modalInstance = $modal.open({
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
		var modalInstance = $modal.open({
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

fabricacionModule.controller('tikal.modules.Fabricacion.fabricante.listCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$modal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $modal, $log) {
	$scope.fabricantes = [];
	$scope.procesaPedimentos = function() {
		$scope.fabricantes = [];
		angular.forEach($scope.pedido.pedimentos, function(pedimento, i) {
			var repetido = false;
			angular.forEach($scope.fabricantes, function(fabricante, j) {
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
				$scope.fabricantes.push(tmpFabricante);
				contactoRaizDao.get({contactId:tmpFabricante.id}).$promise.then(function(data) {
					tmpFabricante.name.name = data.name.name;
				}, function(errResponse) {
				
				});
			}
		});
	};
	$scope.$watch('pedido.pedimentos', function(){
		$scope.procesaPedimentos();
	});
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
				if (pedimento.producto.type == 'ProductoIntermediario') {
					datos.push(pedimento);
				}
				if (pedimento.producto.type == 'ProductoConTalla') {
					var grupo = null;
					angular.forEach(datos, function(value, key) {
						if (value.type == 'GrupoPedimento' && value.producto.lineaDeProductosId == pedimento.producto.lineaDeProductosId) {
							grupo = value;
						}
					});
					if (grupo) {
						grupo.cantidad = grupo.cantidad + pedimento.cantidad;
						pedimento.producto.datosGenerales = grupo.producto.datosGenerales;
						grupo.pedimentos.push(pedimento);
						grupo.pedimentos.sort(function(a,b){
							return a.producto.talla.localeCompare(b.producto.talla);
						});
					} else {
						grupo = {
							type:'GrupoPedimento',
							producto:{
								catalogoId:pedimento.producto.catalogoId,
								lineaDeProductosId:pedimento.producto.lineaDeProductosId,
								datosGenerales:{
									nombre:'',
									descripcion:'',
									unidadMedida:''
								}
							},
							cantidad:0,
							pedimentos:[]
						};
						grupo.pedimentos.push(pedimento);
						grupo.cantidad = pedimento.cantidad;
						pedimento.producto.datosGenerales = grupo.producto.datosGenerales;
						datos.push(grupo);
						//cargar datos generales de la linea
						$scope.lineaDao.get({catalogoId:grupo.producto.catalogoId, lineaProductoId:grupo.producto.lineaDeProductosId}).$promise.then(function(data) {
							grupo.producto.datosGenerales.nombre = data.datosGenerales.nombre;
							grupo.producto.datosGenerales.descripcion = data.datosGenerales.descripcion;
							grupo.producto.datosGenerales.unidadMedida = data.datosGenerales.unidadMedida;
							$scope.pedimentosOrdenados.sort(function(a,b){
								return a.producto.datosGenerales.nombre.localeCompare(b.producto.datosGenerales.nombre);
							});
						}, function(errResponse) {

						});
					}
				}
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
			if (value.producto.catalogoId == partida.producto.catalogoId && value.producto.id == partida.producto.id) {
				total = total + value.cantidad;
			}
		});
		return total;
	};
	var	getTotalPorLinea = function(partida, pedimentos) {
		var total = 0;
		angular.forEach(pedimentos, function(value, key) {
			if (value.producto.lineaDeProductosId && value.producto.catalogoId == partida.linea.catalogoId && 
				value.producto.lineaDeProductosId == partida.linea.id) {
				total = total + value.cantidad;
			}
		});
		return total;
	};
	var	getTotalPorGrupo = function(partida, pedimentos) {
		var total = 0;
		angular.forEach(pedimentos, function(subPedimento, key) {
			angular.forEach(subPedimento, function(value, key) {
				if (partida.producto.type == 'ProductoIntermediario') {
					if (value.producto.catalogoId == partida.producto.catalogoId && value.producto.id == partida.producto.id) {
						total = total + value.cantidad;
					}
				}
				if (partida.producto.type == 'LineaProductosPorTalla') {
					if (value.producto.lineaDeProductosId && value.producto.catalogoId == partida.producto.catalogoId && 
						value.producto.lineaDeProductosId == partida.producto.id) {
						total = total + value.cantidad;
					}
				}
			});
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
'use strict';

var almacenModule = angular.module('tikal.modules.Almacen', ['ngRoute','ngResource','angular-ladda','ngAnimate','ui.bootstrap']);

almacenModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/almacen/entrada', {templateUrl: 'view/almacen/entradas-almacen.html', controller: 'tikal.modules.Almacen.entradas'});
	$routeProvider.when('/almacen/salida', {templateUrl: 'view/almacen/salidas-almacen.html', controller: 'tikal.modules.Almacen.salidas'});
});

almacenModule.controller('tikal.modules.Almacen.entradas', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Entradas Almacen';
	//candidato a pedidos
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPedidos = function() {
		$scope.pedidoDao.query().$promise.then(function(data) {
			$scope.pedidos = data.items;
			$scope.pedidos.sort(function(a,b){
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
	$scope.subPedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadSubPedidos = function(pedidoId) {
		$scope.subPedidoDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			$scope.subPedidos = data.items;
			angular.forEach($scope.subPedidos, function(dato, key) {
				dato.display = dato.puntoEntrega.nombreCorto + ' - ' + dato.puntoEntrega.nombre;
			});
			$scope.subPedidos.sort(function(a,b){
				return a.puntoEntrega.nombreCorto.localeCompare(b.puntoEntrega.nombreCorto);
			});
		}, function(errResponse) {

		});
	};
	$scope.pedimentoDao = $resource(config.address + '/api/pedido/:pedidoId/pedimento',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPedimentos = function(pedidoId) {
		$scope.pedimentoDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			$scope.compartido.pedido.pedimentos = data.items;
		}, function(errResponse) {

		});
	};
	$scope.entradaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/entrada/:entradaId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadEntradas = function(pedidoId) {
		$scope.entradaDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			$scope.compartido.pedido.entradas = data.items;
		}, function(errResponse) {

		});
	};
	$scope.$watch('compartido.pedidoRaiz', function(newValue, oldValue) {
		$scope.compartido.subPedido = null;
		if ($scope.compartido.pedido) {
			$scope.compartido.pedido.pedimentos = null;
			$scope.compartido.pedido.entradas = null;
		}
		$scope.compartido.pedido = null;
		$scope.compartido.fabricantes = null;
		$scope.compartido.fabricante = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id && newValue.type == 'PedidoCompuestoIntermediario') {
					$scope.loadSubPedidos(newValue.id);
				} else {
					$scope.compartido.pedido = newValue;
				}
			} else {
				if (newValue.type == 'PedidoCompuestoIntermediario') {
					$scope.loadSubPedidos(newValue.id);
				} else {
					$scope.compartido.pedido = newValue;
				}
			}
		}
	});
	$scope.$watch('compartido.subPedido', function(newValue, oldValue) {
		if ($scope.compartido.pedido) {
			$scope.compartido.pedido.pedimentos = null;
			$scope.compartido.pedido.entradas = null;
		}
		$scope.compartido.pedido = null;
		$scope.compartido.fabricantes = null;
		$scope.compartido.fabricante = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id) {
					$scope.compartido.pedido = newValue;
				}
			} else {
				$scope.compartido.pedido = newValue;
			}
		}
	});
	//candidato Fabricacion
	$scope.$watch('compartido.pedido', function (newValue, oldValue) {
		if (newValue && newValue.id) {
			$scope.loadPedimentos(newValue.id);
		}
	});
	$scope.procesaPedimentos = function() {
		$scope.compartido.fabricantes = [];
		angular.forEach($scope.compartido.pedido.pedimentos, function(pedimento, i) {
			var repetido = false;
			//pasarlo a un filtro en la vista?
			if (pedimento.type == 'PedimentoIntermediario') {
				pedimento.display = pedimento.producto.datosGenerales.nombre + ' - ' + pedimento.producto.datosGenerales.descripcion;
			}
			if (pedimento.type == 'GrupoPedimento') {
				pedimento.display = pedimento.linea.datosGenerales.nombre + ' - ' + pedimento.linea.datosGenerales.descripcion;
			}
			angular.forEach($scope.compartido.fabricantes, function(fabricante, j) {
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
				$scope.compartido.fabricantes.push(tmpFabricante);
			}
		});
		if ($scope.compartido.fabricantes.length > 0) {
			contactoRaizDao.query({group:'proveedor'}).$promise.then(function(data) {
				var proveedores = data.items;
				angular.forEach($scope.compartido.fabricantes, function(fabricante, i) {
					angular.forEach(proveedores, function(proveedor, j) {
						if (proveedor.id == fabricante.id) {
							fabricante.name.name = proveedor.name.name;
						}
					});
					angular.forEach(fabricante.pedimentos, function(pedimento, key) {
						if (pedimento.type == 'GrupoPedimento') {
							pedimento.pedimentos.sort(function(a,b){
								return a.producto.talla.localeCompare(b.producto.talla);
							});
						}
					});
					fabricante.pedimentos.sort(function(a,b){
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
				});
			}, function(errResponse) {
			
			});
		}
	};
	$scope.$watch('compartido.pedido.pedimentos', function(newValue, oldValue){
		if ($scope.compartido.pedido && $scope.compartido.pedido.pedimentos) {
			$scope.procesaPedimentos();
			$scope.loadEntradas($scope.compartido.pedido.id);
		}
	});
	$scope.procesaEntradas = function() {
		//asigna las entradas por proveedor
		angular.forEach($scope.compartido.fabricantes, function(fabricante, key) {
			fabricante.entradas = [];
			angular.forEach($scope.compartido.pedido.entradas, function(entrada, i) {
				if (entrada.idProveedor == fabricante.id) {
					fabricante.entradas.push(entrada);
				}
			});
			fabricante.listaEntradas = [];
			angular.forEach(fabricante.entradas, function(entrada, i) {
				if (entrada.type=='GrupoRegistroAlmacen') {
					angular.forEach(entrada.registros, function(registro, j) {
						registro.producto.datosGenerales = entrada.linea.datosGenerales;
						fabricante.listaEntradas.push(registro);
					});
				} else {
					fabricante.listaEntradas.push(entrada);
				}
			});
			fabricante.listaEntradas.sort(function(a,b){
				var dateA = new Date(a.fechaRegistro.split('+')[0]);
				var dateB = new Date(b.fechaRegistro.split('+')[0]);
				if (dateA > dateB) {
					return -1;
				}
				if (dateB > dateA) {
					return 1;
				}
				return 0;
			});
		});
		//calcula los restantes por pedimento por fabricante
		angular.forEach($scope.compartido.fabricantes, function(fabricante, i) {
			angular.forEach(fabricante.pedimentos, function(pedimento, j) {
				$scope.calculaTotalesEntrada(pedimento, fabricante.entradas);
			});
		});
		$scope.$broadcast('updateEntradas');
		if ($scope.compartido.fabricante && $scope.compartido.fabricante.listaEntradas) {
			$scope.filtrarEntradas($scope.compartido.fabricante.listaEntradas);
		} else {
			$scope.limpiarFiltro();
		}
	};
	
	$scope.calculaTotalesEntrada = function(pedimento, entradas) {
		if (pedimento.type == 'GrupoPedimento') {
			var total = 0;
			var totalEntrada = 0;
			var totalDevolucion = 0;
			var totalReposicion = 0;
			angular.forEach(pedimento.pedimentos, function(pedimentoGrupo, key) {
				$scope.calculaTotalesEntrada(pedimentoGrupo, entradas);
				total = total + pedimentoGrupo.cantidad;
				totalEntrada = totalEntrada + pedimentoGrupo.totalEntrada;
				totalDevolucion = totalDevolucion + pedimentoGrupo.totalDevolucion;
				totalReposicion = totalReposicion + pedimentoGrupo.totalReposicion;
			});
			pedimento.totalEntrada = totalEntrada;
			pedimento.restante = total - totalEntrada;
			pedimento.totalDevolucion = totalDevolucion;
			pedimento.totalReposicion = totalReposicion;
		} else {
			//pedimento.restante = pedimento.cantidad;
			pedimento.totalEntrada = 0;
			pedimento.totalDevolucion = 0;
			pedimento.totalReposicion = 0;
			angular.forEach(entradas, function(entrada, i) {
				if (entrada.type == 'GrupoRegistroAlmacen') {
					angular.forEach(entrada.registros, function(registro, j) {
						if (pedimento.producto.id == registro.producto.id && pedimento.producto.catalogoId == registro.producto.catalogoId) {
							pedimento.totalEntrada = pedimento.totalEntrada + registro.cantidad;
							if (registro.tag == 'devolucion') {
								pedimento.totalDevolucion = pedimento.totalDevolucion + registro.cantidad;
							}
							if (registro.tag == 'reposicion') {
								pedimento.totalReposicion = pedimento.totalReposicion + registro.cantidad;
							}
						}
					});
				} else {
					if (pedimento.producto.id == entrada.producto.id && pedimento.producto.catalogoId == entrada.producto.catalogoId) {
						pedimento.totalEntrada = pedimento.totalEntrada + entrada.cantidad;
						if (entrada.tag == 'devolucion') {
							pedimento.totalDevolucion = pedimento.totalDevolucion + entrada.cantidad;
						}
						if (entrada.tag == 'reposicion') {
							pedimento.totalReposicion = pedimento.totalReposicion + entrada.cantidad;
						}
					}
				}
			});
			pedimento.restante = pedimento.cantidad - pedimento.totalEntrada;
		}
	};
	// filtro entradas
	$scope.$watch('compartido.fabricante', function(newValue, oldValue){
		if (newValue && newValue.listaEntradas) {
			$scope.filtrarEntradas(newValue.listaEntradas);
		} else {
			$scope.limpiarFiltro();
		}
	});
	$scope.limpiarFiltro = function(entradas) {
		$scope.compartido.filtroEntrada.fechaEntrega = null;
		$scope.compartido.filtroEntrada.pedimento = null;
		$scope.compartido.filtroEntrada.tag = null;
		if (entradas) {
			$scope.filtrarEntradas(entradas);
		}
	};
	$scope.filtrarEntradas = function(entradas) {
		//$log.info('filtrando');
		var result = [];
		angular.forEach(entradas, function(entrada, key) {
			var ok = true;
			//filtro por producto
			if ($scope.compartido.filtroEntrada.pedimento) {
				//$log.info(entrada.producto.type + ' - ' + $scope.compartido.filtroEntrada.pedimento.type);
				if ($scope.compartido.filtroEntrada.pedimento.type == 'PedimentoIntermediario') {
					if (entrada.producto.type == 'ProductoIntermediario') {
						if (entrada.producto.id != $scope.compartido.filtroEntrada.pedimento.producto.id 
							|| entrada.producto.catalogoId != $scope.compartido.filtroEntrada.pedimento.producto.catalogoId) {
							ok = false;
						}
					} else {
						ok = false;
					}
				}
				if ($scope.compartido.filtroEntrada.pedimento.type == 'GrupoPedimento') {
					if (entrada.producto.type == 'ProductoConTalla') {
						if (entrada.producto.lineaDeProductosId != $scope.compartido.filtroEntrada.pedimento.linea.id 
							|| entrada.producto.catalogoId != $scope.compartido.filtroEntrada.pedimento.linea.catalogoId) {
							ok = false;
						}
					} else {
						ok = false;
					}
				}
			}
			//filtro por fecha
			if ($scope.compartido.filtroEntrada.fechaEntrega) {
				var fechaRegistro = new Date(entrada.fechaRegistro.split('+')[0]);
				if (fechaRegistro.getDate() != $scope.compartido.filtroEntrada.fechaEntrega.getDate() 
					|| fechaRegistro.getMonth() != $scope.compartido.filtroEntrada.fechaEntrega.getMonth()
					|| fechaRegistro.getFullYear() != $scope.compartido.filtroEntrada.fechaEntrega.getFullYear()) {
					ok = false;
				}
			}
			//filtro por tag
			if ($scope.compartido.filtroEntrada.tag && $scope.compartido.filtroEntrada.tag != '') {
				if (entrada.tag != $scope.compartido.filtroEntrada.tag) {
					ok = false;
				}
			}
			if (ok) {
				result.push(entrada);
			}
		});
		$scope.compartido.fabricante.listaEntradasFiltradas = result;
	};
	
	$scope.$watch('compartido.pedido.entradas', function(){
		if ($scope.compartido.pedido && $scope.compartido.pedido.entradas) {
			$scope.procesaEntradas();
		}
	});
	$scope.hideShowSub = function(pedimento) {
		if (pedimento.showSub) {
			pedimento.showSub = false;
		} else {
			angular.forEach($scope.compartido.fabricante.pedimentos, function(value, key) {
				if (value.type=='GrupoPedimento') {
					value.showSub = false;
				}
			});
			pedimento.showSub = true;
		}
	};
	$scope.compartido = {
		pedido:{},
		pedimento:null,
		filtroEntrada: {
			fechaEntrega: null
		}
	};
	$scope.loadPedidos();
	//basura para capturar una fecha
	//$scope.minDate = null;
	$scope.maxDate = new Date();
	$scope.dateOptions = {
		formatYear: 'yy',
		startingDay: 1,
		showWeeks:false
	};
	$scope.formatDP = 'dd-MMMM-yyyy';
	$scope.openDP = function($event) {
		$scope.statusDP.opened = true;
	};
	$scope.statusDP = {
		opened: false
	};
	
	$scope.compartido.fechaEntrega = new Date();
	$scope.captura = function(pedimento, subPedimento, size) {
		if ($scope.compartido.fechaEntrega == null) {
			$scope.compartido.fechaEntrega = new Date();
		}
		var elPedimento = pedimento;
		if (subPedimento) {
			subPedimento.producto.datosGenerales = pedimento.linea.datosGenerales;
			elPedimento = subPedimento;
		}
		var tipo = '';
		if (elPedimento.restante + (elPedimento.totalDevolucion + elPedimento.totalReposicion) > 0) {
			tipo = 'entrada';
		} else {
			if (elPedimento.totalEntrada > 0) {
				tipo = 'devolucion';
			} else { 
				tipo = 'reposicion'
			}
		}
		var capturaModel = {
			pedidoRaiz:$scope.compartido.pedidoRaiz,
			pedido:$scope.compartido.pedido,
			subPedido:$scope.compartido.subPedido,
			fabricante:$scope.compartido.fabricante,
			fechaEntrega:$scope.compartido.fechaEntrega,
			formatoFecha:$scope.formatDP,
			opened:false,
			open: function(modelo, event) {
				modelo.opened = true;
			},
			entrada:{
				cantidad:elPedimento.restante + (elPedimento.totalDevolucion + elPedimento.totalReposicion),
				max:elPedimento.restante + (elPedimento.totalDevolucion + elPedimento.totalReposicion)
			},
			devolucion:{
				cantidad:elPedimento.totalEntrada,
				max:elPedimento.totalEntrada
			},
			reposicion:{
				cantidad:(elPedimento.totalDevolucion + elPedimento.totalReposicion) * -1,
				max:(elPedimento.totalDevolucion + elPedimento.totalReposicion) * -1
			},
			pedimento:elPedimento,
			tipo:tipo
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/captura-entrada.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return capturaModel;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.loadEntradas($scope.compartido.pedido.id);
			//ultima fecha capturada
			$scope.compartido.fechaEntrega = modelo.fechaEntrega;
		});
		capturaModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var cantidadTmp = $modalScope.modelo[$modalScope.modelo.tipo].cantidad;
			if ($modalScope.modelo.tipo == 'devolucion') {
				cantidadTmp = cantidadTmp * -1;
			}
			var request = {
				type:'RegistroAlmacenTransient',
				tag:$modalScope.modelo.tipo,
				pedidoId:$modalScope.modelo.pedido.id,
				cantidad:cantidadTmp,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.pedimento.producto.catalogoId,
					id:$modalScope.modelo.pedimento.producto.id
				},
				idProveedor:$modalScope.modelo.fabricante.id,
				fechaRegistro:$modalScope.modelo.fechaEntrega,
				descripcion:$modalScope.modelo.descripcion
			};
			if (!request.descripcion || request.descripcion.length == 0) {
				delete request['descripcion'];
			}
			$scope.entradaDao.save({pedidoId:request.pedidoId}, request).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.editarEntrada = function(entrada, size) {
		var elPedimento = null;
		angular.forEach($scope.compartido.fabricante.pedimentos, function(pedimento, key) {
			if (entrada.producto.type == 'ProductoConTalla') {
				if (pedimento.type == 'GrupoPedimento' && pedimento.linea.id == entrada.producto.lineaDeProductosId && pedimento.linea.catalogoId == entrada.producto.catalogoId) {
					angular.forEach(pedimento.pedimentos, function(subPedimento, i) {
						if (subPedimento.producto.id == entrada.producto.id && subPedimento.producto.catalogoId == entrada.producto.catalogoId) {
							elPedimento = subPedimento;
							elPedimento.producto.datosGenerales = pedimento.linea.datosGenerales;
						}
					});
				}
			}
			if (entrada.producto.type == 'ProductoIntermediario') {
				if (pedimento.type == 'PedimentoIntermediario' && pedimento.producto.id == entrada.producto.id && pedimento.producto.catalogoId == entrada.producto.catalogoId) {
					elPedimento = pedimento;
				}
			}
		});
		var capturaModel = {
			pedidoRaiz:$scope.compartido.pedidoRaiz,
			pedido:$scope.compartido.pedido,
			subPedido:$scope.compartido.subPedido,
			fabricante:$scope.compartido.fabricante,
			fechaRegistro:new Date(entrada.fechaRegistro.split('+')[0]),
			formatoFecha:$scope.formatDP,
			opened:false,
			open: function(modelo, event) {
				modelo.opened = true;
			},
			entrada:{
				cantidad:entrada.cantidad,
				max:elPedimento.restante + (elPedimento.totalDevolucion + elPedimento.totalReposicion) + entrada.cantidad
			},
			devolucion:{
				cantidad:entrada.cantidad * -1,
				max:elPedimento.totalEntrada + entrada.cantidad * -1
			},
			reposicion:{
				cantidad:entrada.cantidad,
				max:(elPedimento.totalDevolucion + elPedimento.totalReposicion) * -1 + entrada.cantidad
			},
			tipo:entrada.tag,
			descripcion:entrada.descripcion,
			pedimento:elPedimento
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/edita-entrada.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return capturaModel;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.loadEntradas($scope.compartido.pedido.id);
		});
		capturaModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var cantidadTmp = $modalScope.modelo[$modalScope.modelo.tipo].cantidad;
			if ($modalScope.modelo.tipo == 'devolucion') {
				cantidadTmp = cantidadTmp * -1;
			}
			var request = {
				type:'RegistroAlmacenTransient',
				tag:$modalScope.modelo.tipo,
				id:entrada.id,
				pedidoId:$modalScope.modelo.pedido.id,
				cantidad:cantidadTmp,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.pedimento.producto.catalogoId,
					id:$modalScope.modelo.pedimento.producto.id
				},
				idProveedor:$modalScope.modelo.fabricante.id,
				fechaRegistro:$modalScope.modelo.fechaRegistro,
				descripcion:$modalScope.modelo.descripcion
			};
			if (!request.descripcion || request.descripcion.length == 0) {
				delete request['descripcion'];
			}
			$scope.entradaDao.save({pedidoId:request.pedidoId, entradaId:entrada.id}, request).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.borrarEntrada = function(entrada, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar la entrega?',
			type:'warning'
		};
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
			$scope.loadEntradas($scope.compartido.pedido.id);
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.entradaDao.delete({pedidoId:entrada.pedidoId,entradaId:entrada.id}).$promise.then(function(data) {
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
	$scope.verNota = function(entrada, size) {
		if (!entrada.descripcion) {
			entrada.descripcion = 'Sin nota';
		}
		var dialogModel = {
			message: entrada.descripcion,
			type:'info'
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/nota-modal.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return dialogModel;
				}
			}
		});
		dialogModel.command = function($modalScope) {
			modalInstance.close($modalScope.modelo);
		};
	};
	$scope.entregaStatus = function(pedimento) {
		if (pedimento.restante > 0) {
			return 'incompleta';
		}
		if (pedimento.restante ==0) {
			return 'completa';
		}
		if (pedimento. restante < 0) {
			return 'excedente';
		}
	};
}]);

almacenModule.controller('tikal.modules.Almacen.salidas', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Salidas Almacen';
	//candidato a pedidos
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPedidos = function() {
		$scope.pedidoDao.query().$promise.then(function(data) {
			$scope.pedidos = data.items;
			$scope.pedidos.sort(function(a,b){
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
	$scope.subPedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadSubPedidos = function(pedidoId) {
		$scope.subPedidoDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			$scope.subPedidos = data.items;
			angular.forEach($scope.subPedidos, function(dato, key) {
				dato.display = dato.puntoEntrega.nombreCorto + ' - ' + dato.puntoEntrega.nombre;
			});
			$scope.subPedidos.sort(function(a,b){
				return a.puntoEntrega.nombreCorto.localeCompare(b.puntoEntrega.nombreCorto);
			});
		}, function(errResponse) {

		});
	};
	$scope.entradaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/entrada/:entradaId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadEntradas = function(pedidoId) {
		$scope.entradaDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			//ordena las entradas
			data.items.sort(function(a,b){
				var objA;
				var objB;
				if (a.type == 'EntradaIntermediario') {
					objA = a.producto.datosGenerales;
				} else {
					objA = a.linea.datosGenerales;
				}
				if (b.type == 'EntradaIntermediario') {
					objB = b.producto.datosGenerales;
				} else {
					objB = b.linea.datosGenerales;
				}
				return objA.nombre.localeCompare(objB.nombre);
			});
			angular.forEach(data.items, function(entrada, key) {
				if (entrada.type == 'GrupoRegistroAlmacen') {
					entrada.registros.sort(function(a,b){
						return a.producto.talla.localeCompare(b.producto.talla);
					});
				}
			});
			//junta las entradas
			var entradasTmp = [];
			var entradaAnterior = null;
			angular.forEach(data.items, function(entrada, i) {
				if (entrada.type == 'GrupoRegistroAlmacen') {
					var registrosTmp = [];
					var registroAnterior = null;
					angular.forEach(entrada.registros, function(registro, j) {
						if (registroAnterior && registroAnterior.producto.id == registro.producto.id && registroAnterior.producto.catalogoId == registro.producto.catalogoId) {
							registroAnterior.cantidad = registroAnterior.cantidad + registro.cantidad;
						} else {
							registrosTmp.push(registro);
							registroAnterior = registro;
						}
					});
					entrada.registros = registrosTmp;
					entradasTmp.push(entrada);
					entradaAnterior = null;
				} else {
					if (entradaAnterior && entradaAnterior.producto.id == entrada.producto.id && entradaAnterior.producto.catalogoId == entrada.producto.catalogoId) {
						entradaAnterior.cantidad = entradaAnterior.cantidad + entrada.cantidad;
					} else {
						entradasTmp.push(entrada);
						entradaAnterior = entrada;
					}
				}
			});
			$scope.compartido.pedido.entradas = entradasTmp;
			$scope.loadSalidas(pedidoId);
		}, function(errResponse) {
		});
	};
	$scope.salidaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/salida/:salidaId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadSalidas = function(pedidoId) {
		$scope.salidaDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
			//pone en linea las salidas
			var listaSalidas = [];
			angular.forEach(data.items, function(salida, i) {
				if (salida.type=='GrupoRegistroAlmacen') {
					angular.forEach(salida.registros, function(registro, j) {
						registro.producto.datosGenerales = salida.linea.datosGenerales;
						listaSalidas.push(registro);
					});
				} else {
					listaSalidas.push(salida);
				}
			});
			listaSalidas.sort(function(a,b){
				var dateA = new Date(a.fechaRegistro.split('+')[0]);
				var dateB = new Date(b.fechaRegistro.split('+')[0]);
				if (dateA > dateB) {
					return -1;
				}
				if (dateB > dateA) {
					return 1;
				}
				return 0;
			});
			$scope.compartido.pedido.listaSalidas = listaSalidas;
			$scope.compartido.pedido.salidas = data.items;
			$scope.loadEnvios(pedidoId);
			//calcula totales
			angular.forEach($scope.compartido.pedido.entradas, function(entrada, i) {
				if (entrada.type == 'GrupoRegistroAlmacen') {
					var totalEnviadoRegistro = 0;
					angular.forEach(entrada.registros, function(registro, j) {
						registro.enviado = 0;
						angular.forEach($scope.compartido.pedido.salidas, function(salida, iKey) {
							if (salida.type == 'GrupoRegistroAlmacen') {
								angular.forEach(salida.registros, function(registroSalida, jKey) {
									if (registro.producto.id == registroSalida.producto.id && registro.producto.catalogoId == registroSalida.producto.catalogoId) {
										registro.enviado = registro.enviado + registroSalida.cantidad;
									}
								});
							}
						});
						registro.restante = registro.cantidad - registro.enviado;
						totalEnviadoRegistro = totalEnviadoRegistro + registro.enviado;
					});
					entrada.enviado = totalEnviadoRegistro;
					entrada.restante = entrada.cantidad - entrada.enviado;
				} else {
					entrada.enviado = 0;
					angular.forEach($scope.compartido.pedido.salidas, function(salida, iKey) {
						if (salida.type == 'SalidaIntermediario') {
							if (entrada.producto.id == salida.producto.id && entrada.producto.catalogoId == salida.producto.catalogoId) {
								entrada.enviado = entrada.enviado + salida.cantidad;
							}
						}
					});
					entrada.restante = entrada.cantidad - entrada.enviado;
				}
			});
			var entradasTmp = [];
			angular.forEach($scope.compartido.pedido.entradas, function(entrada, key) {
				entradasTmp.push(entrada);
			});
			$scope.compartido.pedido.entradas = entradasTmp;
		}, function(errResponse) {
		});
	};
	$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.loadEnvios = function(pedidoId) {
		$scope.envioDao.get({pedidoId:pedidoId}).$promise.then(function(data) {
			data.items.sort(function(a,b){
				return a.name.localeCompare(b.name);
			});
			angular.forEach($scope.compartido.pedido.salidas, function(salida, key) {
				angular.forEach(data.items, function(envio, iKey) {
					if (salida.referenciaEnvio == envio.id) {
						salida.envioName = envio.name;
						salida.envio = envio;
					}
				});
				if (salida.type == 'GrupoRegistroAlmacen') {
					angular.forEach(salida.registros, function(registro, iKey) {
						angular.forEach(data.items, function(envio, jKey) {
							if (registro.referenciaEnvio == envio.id) {
								registro.envioName = envio.name;
								registro.envio = envio;
							}
						});
					});
				}
			});
			var enviosActivos = [];
			angular.forEach(data.items, function(envio, jKey) {
				if (envio.status == 'En preparación') {
					enviosActivos.push(envio);
					$log.info('magia');
				}
			});
			//$log.info('magia' + enviosActivos);
			$scope.compartido.envios = enviosActivos;
		}, function(errResponse) {
		});
	};
	$scope.$watch('compartido.pedidoRaiz', function(newValue, oldValue) {
		$scope.compartido.subPedido = null;
		if ($scope.compartido.pedido) {
			$scope.compartido.pedido.pedimentos = null;
			$scope.compartido.pedido.entradas = null;
		}
		$scope.compartido.pedido = null;
		$scope.compartido.fabricantes = null;
		$scope.compartido.fabricante = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id && newValue.type == 'PedidoCompuestoIntermediario') {
					$scope.loadSubPedidos(newValue.id);
				} else {
					$scope.compartido.pedido = newValue;
				}
			} else {
				if (newValue.type == 'PedidoCompuestoIntermediario') {
					$scope.loadSubPedidos(newValue.id);
				} else {
					$scope.compartido.pedido = newValue;
				}
			}
		}
	});
	$scope.$watch('compartido.subPedido', function(newValue, oldValue) {
		if ($scope.compartido.pedido) {
			$scope.compartido.pedido.pedimentos = null;
			$scope.compartido.pedido.entradas = null;
		}
		$scope.compartido.pedido = null;
		$scope.compartido.fabricantes = null;
		$scope.compartido.fabricante = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id) {
					$scope.compartido.pedido = newValue;
				}
			} else {
				$scope.compartido.pedido = newValue;
			}
		}
	});
	//candidato Fabricacion
	$scope.$watch('compartido.pedido', function (newValue, oldValue) {
		if (newValue && newValue.id) {
			$scope.loadEntradas(newValue.id);
		}
	});
	$scope.hideShowSub = function(partida) {
		if (partida.showSub) {
			partida.showSub = false;
		} else {
			angular.forEach($scope.compartido.pedido.partidas, function(value, key) {
				if (value.type=='GrupoRegistroAlmacen') {
					value.showSub = false;
				}
			});
			partida.showSub = true;
		}
	};
	$scope.compartido = {
		pedido:{},
		filtroEntrada: {
			fechaEntrega: null
		}
	};
	$scope.loadPedidos();
	//basura para capturar una fecha
	//$scope.minDate = null;
	$scope.maxDate = new Date();
	$scope.dateOptions = {
		formatYear: 'yy',
		startingDay: 1,
		showWeeks:false
	};
	$scope.formatDP = 'dd-MMMM-yyyy';
	$scope.openDP = function($event) {
		$scope.statusDP.opened = true;
	};
	$scope.statusDP = {
		opened: false
	};
	
	$scope.compartido.fechaEntrega = new Date();
	$scope.captura = function(registro, subRegistro, size) {
		if ($scope.compartido.fechaEntrega == null) {
			$scope.compartido.fechaEntrega = new Date();
		}
		var elRegistro = registro;
		if (subRegistro) {
			subRegistro.producto.datosGenerales = registro.linea.datosGenerales;
			elRegistro = subRegistro;
		}
		if (!$scope.compartido.envio) {
			if ($scope.compartido.envios && $scope.compartido.envios.length > 0) {
				$scope.compartido.envio =  $scope.compartido.envios[0];
			}
		}
		var capturaModel = {
			pedidoRaiz:$scope.compartido.pedidoRaiz,
			pedido:$scope.compartido.pedido,
			subPedido:$scope.compartido.subPedido,
			fechaEntrega:$scope.compartido.fechaEntrega,
			formatoFecha:$scope.formatDP,
			opened:false,
			open: function(modelo, event) {
				modelo.opened = true;
			},
			salida:{
				cantidad:elRegistro.restante,
				max:elRegistro.restante
			},
			registro:elRegistro,
			envio:$scope.compartido.envio,
			envios:$scope.compartido.envios
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/captura-salida.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return capturaModel;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.loadSalidas($scope.compartido.pedido.id);
			//ultima fecha capturada
			$scope.compartido.fechaEntrega = modelo.fechaEntrega;
			//ultimo envio capturado
			$scope.compartido.envio = modelo.envio;
		});
		capturaModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var request = {
				type:'RegistroAlmacenTransient',
				tag:'salidaEnvio',
				pedidoId:$modalScope.modelo.pedido.id,
				cantidad:$modalScope.modelo.salida.cantidad,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.registro.producto.catalogoId,
					id:$modalScope.modelo.registro.producto.id
				},
				idProveedor:0,//quitar esta aberracion, culpa de validaciones en annotations
				referenciaEnvio:$modalScope.modelo.envio.id,
				fechaRegistro:$modalScope.modelo.fechaEntrega,
				descripcion:$modalScope.modelo.descripcion
			};
			if (!request.descripcion || request.descripcion.length == 0) {
				delete request['descripcion'];
			}
			$scope.salidaDao.save({pedidoId:request.pedidoId}, request).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.editar = function(registro, size) {
		var laEntrada = null;
		angular.forEach($scope.compartido.pedido.entradas, function(entrada, i) {
			if (registro.producto.type == 'ProductoConTalla' && entrada.type == 'GrupoRegistroAlmacen') {
				angular.forEach(entrada.registros, function(subEntrada, j) {
					if (subEntrada.producto.id == registro.producto.id && subEntrada.producto.catalogoId == registro.producto.catalogoId) {
						laEntrada = subEntrada;
					}
				});
			}
			if (registro.producto.type == 'ProductoIntermediario' && entrada.type == 'EntradaIntermediario') {
				if (entrada.producto.id == registro.producto.id && entrada.producto.catalogoId == registro.producto.catalogoId) {
					laEntrada = entrada;
				}
			}
		});
		var capturaModel = {
			pedidoRaiz:$scope.compartido.pedidoRaiz,
			pedido:$scope.compartido.pedido,
			subPedido:$scope.compartido.subPedido,
			fabricante:$scope.compartido.fabricante,
			fechaRegistro:new Date(registro.fechaRegistro.split('+')[0]),
			formatoFecha:$scope.formatDP,
			opened:false,
			open: function(modelo, event) {
				modelo.opened = true;
			},
			salida:{
				cantidad:registro.cantidad,
				max:laEntrada.restante + registro.cantidad
			},
			envio:registro.envio,
			envios:$scope.compartido.envios,
			descripcion:registro.descripcion,
			registro:registro
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/edita-salida.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return capturaModel;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.loadEntradas($scope.compartido.pedido.id);
		});
		capturaModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var request = {
				type:'RegistroAlmacenTransient',
				tag:$modalScope.modelo.tipo,
				id:registro.id,
				pedidoId:$modalScope.modelo.pedido.id,
				cantidad:$modalScope.modelo.salida.cantidad,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.registro.producto.catalogoId,
					id:$modalScope.modelo.registro.producto.id
				},
				idProveedor:0,//quitar esta aberracion, culpa de validaciones en annotations
				referenciaEnvio:$modalScope.modelo.envio.id,
				fechaRegistro:$modalScope.modelo.fechaRegistro,
				descripcion:$modalScope.modelo.descripcion
			};
			if (!request.descripcion || request.descripcion.length == 0) {
				delete request['descripcion'];
			}
			$scope.salidaDao.save({pedidoId:request.pedidoId, salidaId:registro.id}, request).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.borrar = function(registro, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar la salida?',
			type:'warning'
		};
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
			$scope.loadEntradas($scope.compartido.pedido.id);
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.salidaDao.delete({pedidoId:registro.pedidoId,salidaId:registro.id}).$promise.then(function(data) {
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
	$scope.verNota = function(entrada, size) {
		if (!entrada.descripcion) {
			entrada.descripcion = 'Sin nota';
		}
		var dialogModel = {
			message: entrada.descripcion,
			type:'info'
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/almacen/nota-modal.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return dialogModel;
				}
			}
		});
		dialogModel.command = function($modalScope) {
			modalInstance.close($modalScope.modelo);
		};
	};
	$scope.entregaStatus = function(pedimento) {
		if (pedimento.restante > 0) {
			return 'incompleta';
		}
		if (pedimento.restante ==0) {
			return 'completa';
		}
		if (pedimento. restante < 0) {
			return 'excedente';
		}
	};
}]);

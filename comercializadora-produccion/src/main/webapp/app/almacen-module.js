'use strict';

var almacenModule = angular.module('tikal.modules.Almacen', ['ngRoute','ngResource','angular-ladda','ngAnimate','ui.bootstrap']);

almacenModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/almacen', {templateUrl: 'view/almacen/inicio-almacen.html', controller: 'tikal.modules.Almacen.inicio'});
});

almacenModule.controller('tikal.modules.Almacen.inicio', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, contactoRaizDao, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Almacen';
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
				if (dateA < dateB) {
					return -1;
				}
				if (dateB < dateA) {
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
	};
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
		pedimento:null
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
	/*$scope.disabledDP = function(date, mode) {
		return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
	};*/
	//
	$scope.compartido.fechaEntrega = new Date();
	$scope.capturaEntrada = function(pedimento, subPedimento, size) {
		if ($scope.compartido.fechaEntrega == null) {
			$scope.compartido.fechaEntrega = new Date();
		}
		var elPedimento = pedimento;
		if (subPedimento) {
			subPedimento.producto.datosGenerales = pedimento.linea.datosGenerales;
			elPedimento = subPedimento;
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
			max:elPedimento.restante,
			cantidad:elPedimento.restante,
			pedimento:elPedimento
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
			var request = {
				type:'RegistroAlmacenTransient',
				pedidoId:$modalScope.modelo.pedido.id,
				cantidad:$modalScope.modelo.cantidad,
				producto:{
					type:'ProductoReference',
					catalogoId:$modalScope.modelo.pedimento.producto.catalogoId,
					id:$modalScope.modelo.pedimento.producto.id
				},
				idProveedor:$modalScope.modelo.fabricante.id,
				fechaRegistro:$modalScope.modelo.fechaEntrega
			};
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

'use strict';

var pedidoModule = angular.module('tikal.modules.Pedido', ['ngRoute','ngResource','angular-ladda']);

pedidoModule.constant('PRODUCCION_REMOTE_ADDRESS', {
	address: 'https://tolumex-dev.appspot.com',
});

pedidoModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/pedido', {templateUrl: 'view/pedido/pedido-list.html', controller: 'tikal.modules.Pedido.PedidoListCtrl'});
	$routeProvider.when('/pedido/:pedidoId', {templateUrl: 'view/pedido/pedido-detail.html', controller: 'tikal.modules.Pedido.PedidoDetailCtrl'});
	$routeProvider.when('/pedido/:pedidoId/subPedido/:subPedidoId', {templateUrl: 'view/pedido/subPedido-detail.html', controller: 'tikal.modules.Pedido.subPedido.PedidoDetailCtrl'});
	$routeProvider.when('/pedido/:pedidoId/cliente', {templateUrl: 'view/pedido-cliente.html', controller: 'PedidoClienteCtrl'});
	$routeProvider.when('/pedido/:pedidoId/cliente/contacto/:contactoId', {templateUrl: 'view/pedido-cliente-contacto.html', controller: 'ContactoClienteDetailCtrl'});
	$routeProvider.when('/pedido/:pedidoId/seleccion/cliente/', {templateUrl: 'view/contacto/contacto-list.html', controller: 'tikal.modules.Pedido.PedidoContactoSeleccionCtrl'});
	$routeProvider.when('/pedido/:pedidoId/partida/:partidaId', {templateUrl: 'view/partida-detail.html', controller: 'PartidaDetailCtrl'});
	$routeProvider.when('/pedido/:pedidoId/partida/:partidaId/subProducto/:subProductoId', {templateUrl: 'view/sub-producto-detail.html', controller: 'SubProductoDetailCtrl'});
	$routeProvider.when('/pedido/:pedidoId/puntoEntrega', {templateUrl: 'view/pedido/puntoEntrega/punto-entrega-list.html', controller: 'tikal.modules.Pedido.puntoEntrega.ListCtrl'});
	$routeProvider.when('/pedido/:pedidoId/puntoEntrega/:puntoEntregaId', {templateUrl: 'view/pedido/puntoEntrega/punto-entrega-detail.html', controller: 'tikal.modules.Pedido.puntoEntrega.DetailCtrl'});
	
});

pedidoModule.controller('tikal.modules.Pedido.PedidoListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Pedidos';
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
	
	$scope.nuevoPedido = function(tipo, size) {
		$scope.modeloNuevo = {
			type:tipo,
			nombre:'',
			command: function($modalScope) {
				$scope.pedidoDao.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					$location.path($location.path() + '/' + newId);
				}).$promise.then(
					function(data) {}, 
					function(errResponse) {
						//borrar el cliente que se creo!!! o quedara huerfano
						$modalScope.actionLoading = false;
				});
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/pedido-nuevo.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevo;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//$scope.modelo.primaryContact.address[index] = angular.copy(modelo);
		});
	};
	$scope.borrarPedido = function(pedidoId, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar el pedido?',
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
			
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.pedidoDao.delete({pedidoId:pedidoId}).$promise.then(
				function(data) {
					$scope.loadPedidos();
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});
		};
	};
}]);
  
pedidoModule.controller('tikal.modules.Pedido.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
	'$uibModal', 'contactoRaizDao','$window', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $uibModal, contactoRaizDao, $window, jsonUtil) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Pedidos',
		link: '/pedido/'
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
		if ($scope.pedido.idCliente) {
			contactoRaizDao.get({contactId:$scope.pedido.idCliente}).$promise.then(function(data) {
				$scope.cliente = data;
			}, function(errResponse) {
			
			});
		}
	}, function(errResponse) {
	});
	$scope.verCliente = function() {
		$location.path($location.path() + '/cliente/' + $scope.cliente.id);
	};
	$scope.seleccionarCliente = function() {
		$location.path($location.path() + '/seleccion/cliente/');
	};
	$scope.editarHeader = function(size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/pedido-edicion-header.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.pedido;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.pedido = modelo;
			$window.sessionStorage.setItem('nombrePedidoActual', $scope.pedido.nombre);
			$scope.navegacion.actual.display = $scope.pedido.nombre;
		});
		$scope.pedido.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var tmpModelo = angular.copy($modalScope.modelo);
			delete tmpModelo['pedimentos'];
			$scope.pedidoDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	var addAddressPart = function(part, context) {
		if (part) {
			if (context.comenzado) {
				context.response = context.response + ', ' + part;
			} else {
				context.response = part;
				context.comenzado = true;
			}
		}
	};
	$scope.getFullAddress = function(address) {
		if (!address) {
			return '';
		}
		var context = {
			comenzado:false,
			response:''
		};
		if (address.type == 'MexicoAddress') {
			if (address.numeroExterior && address.calle) {
				context.response = address.numeroExterior + ' ' + address.calle;
				context.comenzado = true;
			} else {
				addAddressPart(address.numeroExterior, context);
				addAddressPart(address.calle, context);
			}
			addAddressPart(address.colonia, context);
			addAddressPart(address.codigoPostal, context);
			addAddressPart(address.ciudad, context);
			addAddressPart(address.estado, context);
		}
		return context.response;
	};
	$scope.editarPuntoEntrega = function(size) {
		var modeloPuntoEntrega;
		if ($scope.pedido.puntoEntrega) {
			modeloPuntoEntrega = $scope.pedido.puntoEntrega;
		} else {
			modeloPuntoEntrega = {};
		}
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/punto-entrega-edit.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return modeloPuntoEntrega;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.pedido.puntoEntrega = modelo;
		});
		modeloPuntoEntrega.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var puntoEntregaTmp = angular.copy($modalScope.modelo);
			puntoEntregaTmp.type = 'PuntoEntregaOfy';
			if (puntoEntregaTmp.direccion) {
				puntoEntregaTmp.direccion.type = 'MexicoAddress';
			} else {
				puntoEntregaTmp.direccion = {
					type:'MexicoAddress'
				};
			}
			jsonUtil.clean(puntoEntregaTmp.direccion);
			var addressPropCount=0;
			angular.forEach(puntoEntregaTmp.direccion, function(value, key) {
				addressPropCount = addressPropCount + 1;
			});
			if (addressPropCount < 2 ) {
				delete puntoEntregaTmp['direccion'];
			}
			var tmpModelo = angular.copy($scope.pedido);
			tmpModelo.puntoEntrega = puntoEntregaTmp;
			delete tmpModelo['pedimentos'];
			$scope.pedidoDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close(puntoEntregaTmp);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.borrarPuntoEntrega = function(size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar los datos del punto de entrega?',
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
			delete $scope.pedido['puntoEntrega'];
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var tmpModelo = angular.copy($scope.pedido);
			delete tmpModelo['puntoEntrega'];
			$scope.pedidoDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close(tmpModelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});
		};
	};
}]);

pedidoModule.controller('tikal.modules.Pedido.ProductoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.partidas = [];
	$scope.sumaPartidas = [];
	var cargaPartidaCount = 0;
	$scope.partidaDao = $resource(config.address + '/api/pedido/:pedidoId/partida/:partidaId',{partidaId:'@id'}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadPartidas = function(pedido) {
		cargaPartidaCount = cargaPartidaCount + 1;
		$scope.partidaDao.query({pedidoId:pedido.id}).$promise.then(function(data) {
			var grupos = [];
			angular.forEach($scope.sumaPartidas, function(partida, key) {
				grupos.push(partida);
			});
			angular.forEach(data.items, function(partida, key) {
				partida.pedido = pedido;
				if (partida.type == 'GrupoPartida') {
					angular.forEach(partida.partidas, function(subPartida, key) {
						subPartida.pedido = pedido;
					});
				}
				var grupo = null;
				angular.forEach(grupos, function(item, key) {
					if (partida.type == 'PartidaIntermediario') {
						if (partida.producto.id == item.producto.id && partida.producto.catalogoId == item.producto.catalogoId) {
							grupo = item;
						}
					} else {
						if (partida.linea.id == item.producto.id && partida.linea.catalogoId == item.producto.catalogoId) {
							grupo = item;
						}
					}
				});
				if (grupo) {
					grupo.partidas.push(partida);
					grupo.cantidad = grupo.cantidad + partida.cantidad;
					grupo.partidas.sort(function(a,b){
						return a.pedido.puntoEntrega.nombreCorto.localeCompare(b.pedido.puntoEntrega.nombreCorto);
					});
				} else {
					var nuevoGrupo = {
						type:'GrupoProducto',
						pedidoId:$scope.pedido.id,
						cantidad:partida.cantidad,
						partidas:[]
					}
					if (partida.producto) {
						nuevoGrupo.producto = partida.producto;
					} else {
						nuevoGrupo.producto = partida.linea;
					}
					nuevoGrupo.partidas.push(partida);
					grupos.push(nuevoGrupo);
				}
			});
			grupos.sort(function(a,b){
				return a.producto.datosGenerales.nombre.localeCompare(b.producto.datosGenerales.nombre);
			});
			$scope.sumaPartidas = grupos;
			cargaPartidaCount = cargaPartidaCount - 1;
			if (cargaPartidaCount < 1) {
				$scope.partidas = $scope.sumaPartidas;
			}
		}, function(errResponse) {

		});
	};
	$scope.$watch('subPedidos', function(newValue, oldValue) {
		if (newValue && newValue.items) {
			var tmpQueue = [];
			angular.forEach(newValue.items, function(pedido, key) {
				tmpQueue.push(pedido);
			});
			$scope.loadPartidaQueue = tmpQueue;
		}
	});
	$scope.loadPartidaQueue = [];
	$scope.$watch('loadPartidaQueue', function(newValue, oldValue) {
		angular.forEach($scope.loadPartidaQueue, function(pedido, key) {
			$scope.loadPartidas(pedido);
		});
		/*if (newValue.length > 0) {
			$scope.loadPartidas(newValue[0]);
			var tmpQueue = [];
			angular.forEach(newValue, function(elemento, key) {
				if (key != 0) {
					tmpQueue.push(elemento);
				}
			});
			//mover esto a otra parte si se quiere hacer secuencial o paralelo
			//asi funciona en paralelo
			$scope.loadPartidaQueue = tmpQueue;
		}*/
	});
	$scope.hideShowSubPartida = function(param) {
		if ($scope.ultimaEdicionId == param.producto.id) {
			$scope.ultimaEdicionId = null;
			param.showSub = false;
		} else {
			if (param.showSub) {
				param.showSub = false;
			} else {
				angular.forEach($scope.partidas, function(partida, key) {
					partida.showSub = false;
				});
				param.showSub = true;
				$scope.ultimaEdicionId = param.producto.id;
			}
		}
	}
}]);

pedidoModule.controller('tikal.modules.Pedido.subpedido.PedidoListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log, jsonUtil) {
    $scope.subPedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadSubPedidos = function() {
		$scope.subPedidoDao.query().$promise.then(function(data) {
			$scope.subPedidos = data;
			$scope.subPedidos.items.sort(function(a,b){
				return a.puntoEntrega.nombreCorto.localeCompare(b.puntoEntrega.nombreCorto);
			});
		}, function(errResponse) {

		});
	};
	$scope.loadSubPedidos();
	$scope.verSubPedido = function(pedidoId) {
	  $location.path($location.path() + '/subPedido/' + pedidoId);
	};
	
	$scope.nuevoSubPedido = function(tipo, size) {
		var modeloPuntoEntrega = {
			type:'PuntoEntregaOfy',
			direccion:{
				type:'MexicoAddress'
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/punto-entrega-edit.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return modeloPuntoEntrega;
				}
			}
		});
		/*modalInstance.result.then(function (modelo) {
		});*/
		modeloPuntoEntrega.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var puntoEntregaTmp = angular.copy($modalScope.modelo);
			jsonUtil.clean(puntoEntregaTmp.direccion);
			var addressPropCount=0;
			angular.forEach(puntoEntregaTmp.direccion, function(value, key) {
				addressPropCount = addressPropCount + 1;
			});
			if (addressPropCount < 2 ) {
				delete puntoEntregaTmp['direccion'];
			}
			var modeloNuevoPedido = {
				type:tipo,
				puntoEntrega:puntoEntregaTmp
			};
			$scope.subPedidoDao.save(modeloNuevoPedido, function (data, headers) {
				var newId = headers('Location').split('/').pop();
				$location.path($location.path() + '/subPedido/' + newId);
			}).$promise.then(
				function(data) {}, 
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.borrarSubPedido = function(subPedidoId, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar el pedido?',
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
			
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.subPedidoDao.delete({subPedidoId:subPedidoId}).$promise.then(
				function(data) {
					$scope.loadSubPedidos();
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});
		};
	};
}]);

pedidoModule.controller('tikal.modules.Pedido.subPedido.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
	'$uibModal', 'contactoRaizDao','$window', 'tikal.modules.Util.Json',
  function ($scope, $resource, $routeParams, config, $log, $location, $uibModal, contactoRaizDao, $window, jsonUtil) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	};
	$scope.navegacion.previos.push({
		display: 'Pedidos',
		link: '/pedido/'
	});
	$scope.navegacion.previos.push({
		display: $window.sessionStorage.getItem('nombrePedidoActual'),
		link: '/pedido/' + $routeParams.pedidoId
	});
	$scope.pedidoRaizId = $routeParams.pedidoId;
	$scope.currentPedidoId = $routeParams.subPedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId, subPedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		var tmp = $scope.pedido;
		$scope.pedido = data;
		$scope.pedido.pedimentos = tmp;
		//$scope.$broadcast('pedidoCargado', $scope.pedido);
		//$scope.$emit('pedidoCargado', $scope.pedido);
		$scope.navegacion.actual.display = $scope.pedido.puntoEntrega.nombreCorto;
	}, function(errResponse) {
	});
	var addAddressPart = function(part, context) {
		if (part) {
			if (context.comenzado) {
				context.response = context.response + ', ' + part;
			} else {
				context.response = part;
				context.comenzado = true;
			}
		}
	};
	$scope.getFullAddress = function(address) {
		if (!address) {
			return '';
		}
		var context = {
			comenzado:false,
			response:''
		};
		if (address.type == 'MexicoAddress') {
			if (address.numeroExterior && address.calle) {
				context.response = address.numeroExterior + ' ' + address.calle;
				context.comenzado = true;
			} else {
				addAddressPart(address.numeroExterior, context);
				addAddressPart(address.calle, context);
			}
			addAddressPart(address.colonia, context);
			addAddressPart(address.codigoPostal, context);
			addAddressPart(address.ciudad, context);
			addAddressPart(address.estado, context);
		}
		return context.response;
	};
	$scope.editarPuntoEntrega = function(size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/punto-entrega-edit.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.pedido.puntoEntrega;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.pedido.puntoEntrega = modelo;
			$scope.navegacion.actual.display = $scope.pedido.puntoEntrega.nombreCorto;
		});
		$scope.pedido.puntoEntrega.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var puntoEntregaTmp = angular.copy($modalScope.modelo);
			if (puntoEntregaTmp.direccion) {
				puntoEntregaTmp.direccion.type = 'MexicoAddress';
			} else {
				puntoEntregaTmp.direccion = {
					type:'MexicoAddress'
				};
			}
			jsonUtil.clean(puntoEntregaTmp.direccion);
			var addressPropCount=0;
			angular.forEach(puntoEntregaTmp.direccion, function(value, key) {
				addressPropCount = addressPropCount + 1;
			});
			if (addressPropCount < 2 ) {
				delete puntoEntregaTmp['direccion'];
			}
			var pedidoTmp = angular.copy($scope.pedido);
			pedidoTmp.puntoEntrega = puntoEntregaTmp;
			delete pedidoTmp['pedimentos'];
			$scope.pedidoDao.save(pedidoTmp).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close(puntoEntregaTmp);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
}]);

pedidoModule.controller('tikal.modules.Pedido.PedidoContactoSeleccionCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'CONTACTO_REMOTE_ADDRESS', 
	'$location', '$uibModal', '$window','$log',
  function ($scope, $resource, $routeParams, config, configContacto, $location, $uibModal, $window, $log) {
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Pedidos',
		link: '/pedido/'
	});
	$scope.navegacion.previos.push({
		display: $window.sessionStorage.getItem('nombrePedidoActual'),
		link: '/pedido/' + $routeParams.pedidoId
	});
	$scope.navegacion.actual.display = 'Seleccion Cliente';
	$scope.group="Clientes";
	$scope.nuevoActionName="Cliente";
	var contactoDao = $resource(configContacto.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'query':  {method:'GET', isArray:false, params:{group:'cliente'}}
	});
	contactoDao.query().$promise.then(function(data) {
		$scope.contactos = data;
	}, function(errResponse) {

	});
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
	}, function(errResponse) {
	});
	$scope.verContacto = function(contactoId) {
		if (!$scope.actionLoading) {
			var tmpModelo = angular.copy($scope.pedido);
			tmpModelo.idCliente = contactoId;
			$scope.actionLoading = true;
			$scope.pedidoDao.save(tmpModelo).$promise.then(
				function(data) {
					$scope.actionLoading = false;
					$location.path('/pedido/' + $routeParams.pedidoId);
				}, function(errResponse) {
					$scope.actionLoading = false;
			});
		}
	};
	$scope.nuevoContacto = function(size) {
		$scope.modeloNuevoContacto = {
			type:"ClienteMx",
			name: {
				type: "OrganizationName",
				name: ""
			},
			group:{
				name:'cliente'
			},
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				contactoDao.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					var tmpModelo = angular.copy($scope.pedido);
					tmpModelo.idCliente = newId;
					$scope.actionLoading = true;
					$scope.pedidoDao.save(tmpModelo).$promise.then(
						function(data) {
							$scope.actionLoading = false;
							$location.path('/pedido/' + $routeParams.pedidoId);
						}, function(errResponse) {
							$scope.actionLoading = false;
					});
				}).$promise.then(
					function(data) {}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/contacto/contacto-nuevo.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevoContacto;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//$scope.modelo.primaryContact.address[index] = angular.copy(modelo);
		});
	};
}]);

pedidoModule.controller('tikal.modules.Pedido.partida.PartidaCrud.query', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
    $scope.loadPartidas = function() {
		$scope.partidaDao.query().$promise.then(function(data) {
			$scope.partidas = data.items;
			$scope.partidas.sort(function(a,b){
				var nombreA;
				if (a.producto) {
					nombreA = a.producto.datosGenerales.nombre;
				} else {
					nombreA = a.linea.datosGenerales.nombre;
				}
				var nombreB;
				if (b.producto) {
					nombreB = b.producto.datosGenerales.nombre;
				} else {
					nombreB = b.linea.datosGenerales.nombre;
				}
				return nombreA.localeCompare(nombreB);
			});
			angular.forEach($scope.partidas, function(partida, key) {
				partida.pedido = $scope.pedido;
				if (partida.type == 'GrupoPartida') {
					angular.forEach(partida.partidas, function(subPartida, key) {
						subPartida.pedido = $scope.pedido;
					});
					partida.partidas.sort(function(a,b){
						return a.producto.talla.localeCompare(b.producto.talla);
					});
				}
			});
		}, function(errResponse) {

		});
	};
	$scope.$watch('pedido', function () {
		if ($scope.pedido && $scope.pedido.id && $scope.pedido.type != 'PedidoCompuestoIntermediario') {
			$scope.loadPartidas();
		}
	});
}]);

pedidoModule.controller('tikal.modules.Pedido.partida.PartidaCrudCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
    $scope.partidaDao = $resource(config.address + '/api/pedido/:pedidoId/partida/:partidaId',{pedidoId:$scope.currentPedidoId, partidaId:'@id'}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.seleccionProductoActionName = 'Partida';
	$scope.$on('seleccionProducto', function (event, data) {
		$scope.agregarPartidaProducto(data);
	});
	$scope.$on('seleccionSubProducto', function (event, data) {
		$scope.agregarPartidaLinea(data);
	});
	$scope.editarPartida = function(partida, size) {
		partida.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var producto = {
				type:'ProductoReference',
				catalogoId:$modalScope.modelo.producto.catalogoId,
				id:$modalScope.modelo.producto.id
			};
			var tmpModelo = angular.copy($modalScope.modelo);
			tmpModelo.type = 'PartidaIntermediarioTransient';
			tmpModelo.producto = producto;
			delete tmpModelo['fechaDeCreacion'];
			delete tmpModelo['pedido'];
			$scope.partidaDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, 
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/partida-producto-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return partida;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			partida.cantidad = modelo.cantidad;
		});
	};
	$scope.editarSubPartida = function(partida, subPartida, size) {
		var modeloEdicion = angular.copy(subPartida);
		modeloEdicion.producto.datosGenerales = partida.linea.datosGenerales;
		modeloEdicion.command = function($modalScope) {
			$modalScope.actionLoading = true;
			var producto = {
				type:'ProductoReference',
				catalogoId:$modalScope.modelo.producto.catalogoId,
				id:$modalScope.modelo.producto.id
			};
			var tmpModelo = angular.copy($modalScope.modelo);
			tmpModelo.type = 'PartidaIntermediarioTransient';
			tmpModelo.producto = producto;
			delete tmpModelo['fechaDeCreacion'];
			delete tmpModelo['datosGenerales'];
			delete tmpModelo['pedido'];
			$scope.partidaDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, 
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/partida-producto-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return modeloEdicion;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			subPartida.cantidad = modelo.cantidad;
			var total = 0;
			//sumar
			angular.forEach(partida.partidas, function(subPartida, key) {
				total = total + subPartida.cantidad;
			});
			partida.cantidad = total;
		});
	};
	$scope.borrarPartida = function(partida, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar la partida?',
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
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.partidaDao.delete({partidaId:partida.id}).$promise.then(
				function(data) {
					$scope.loadPartidas($scope.currentPedidoId);
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});
		};
	};
	$scope.agregarPartidaProducto = function(producto, size) {
		$scope.modeloNuevaPartida = {
			type:"PartidaIntermediarioTransient",
			producto:producto,
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				var modeloTmp = angular.copy($modalScope.modelo);
				modeloTmp.producto = {
					type:'ProductoReference',
					id:modeloTmp.producto.id,
					catalogoId:modeloTmp.producto.catalogoId
				};
				$scope.partidaDao.save(modeloTmp).$promise.then(
					function(data) {
						$scope.loadPartidas($scope.currentPedidoId);
						$modalScope.actionLoading = false;
						modalInstance.close($modalScope.modelo);
					}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/partida-producto-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevaPartida;
				}
			}
		});
		//modalInstance.result.then(function (modelo) {
			//$scope.modelo.primaryContact.address[index] = angular.copy(modelo);
		//});
	};
	$scope.agregarPartidaLinea = function(producto, size) {
		$scope.modeloNuevaPartida = {
			type:"PartidaIntermediarioTransient",
			producto:producto.subProducto,
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				var modeloTmp = angular.copy($modalScope.modelo);
				modeloTmp.producto = {
					type:'ProductoReference',
					id:modeloTmp.producto.id,
					catalogoId:modeloTmp.producto.catalogoId
				};
				$scope.partidaDao.save(modeloTmp).$promise.then(
					function(data) {
						$scope.loadPartidas($scope.currentPedidoId);
						$modalScope.actionLoading = false;
						modalInstance.close($modalScope.modelo);
					}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/pedido/partida-producto-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevaPartida;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//$scope.subProductoSeleccionado = null;
			$scope.$broadcast('limpiarSeleccionSubProducto', {});
			$scope.ultimaEdicionId = producto.id;
			document.querySelector('#tallaSelector').focus();
		});
	};
	$scope.hideShowSubPartida = function(param) {
		if ($scope.ultimaEdicionId == param.linea.id) {
			$scope.ultimaEdicionId = null;
			param.showSub = false;
		} else {
			if (param.showSub) {
				param.showSub = false;
			} else {
				angular.forEach($scope.partidas, function(partida, key) {
					if (partida.type == 'GrupoPartida') {
						partida.showSub = false;
					}
				});
				param.showSub = true;
				$scope.ultimaEdicionId = param.linea.id;
			}
		}
	}
}]);

pedidoModule.directive('partidaList', function() {
  return {
    restrict: 'E',
	controller:'tikal.modules.Pedido.partida.PartidaCrud.query',
    templateUrl: 'view/pedido/partida-list.html'
  };
});

pedidoModule.directive('subPedidoList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/pedido/subPedido-list.html'
  };
});

pedidoModule.directive('pedidoProductoList', function() {
  return {
    restrict: 'E',
	controller:'tikal.modules.Pedido.ProductoDetailCtrl',
    templateUrl: 'view/pedido/pedido-producto-list.html'
  };
});
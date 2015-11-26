'use strict';

var envioModule = angular.module('tikal.modules.Envio', ['ngRoute','ngResource','angular-ladda']);

envioModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/envio', {templateUrl: 'view/envio/pedido-list.html', controller: 'tikal.modules.Envio.PedidoListCtrl'});
	$routeProvider.when('/envio/:pedidoId', {templateUrl: 'view/envio/pedido-detail.html', controller: 'tikal.modules.Envio.PedidoDetailCtrl'});
	$routeProvider.when('/envio/:pedidoId/subPedido/:subPedidoId', {templateUrl: 'view/envio/pedido-detail.html', controller: 'tikal.modules.Envio.subPedido.PedidoDetailCtrl'});
	$routeProvider.when('/envio/:pedidoId/envio/:envioId', {templateUrl: 'view/envio/envio-detail.html', controller: 'tikal.modules.Envio.EnvioDetailCtrl'});
	$routeProvider.when('/envio/:pedidoId/subPedido/:subPedidoId/envio/:envioId', {templateUrl: 'view/envio/envio-detail.html', controller: 'tikal.modules.Envio.subPedido.EnvioDetailCtrl'});
	
	$routeProvider.when('/envio/:pedidoId/envio/:envioId/seleccion/transportista/', {templateUrl: 'view/contacto/contacto-list.html', controller: 'tikal.modules.Envio.PedidoContactoSeleccionCtrl'});
	$routeProvider.when('/envio/:pedidoId/subPedido/:subPedidoId/envio/:envioId/seleccion/transportista/', 
		{templateUrl: 'view/contacto/contacto-list.html', controller: 'tikal.modules.Envio.subPedido.PedidoContactoSeleccionCtrl'});
});

envioModule.controller('tikal.modules.Envio.PedidoListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
    $scope.edicion = true;
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.actual.display = 'Envíos';
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

envioModule.controller('tikal.modules.Envio.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
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
		display: 'Envíos',
		link: '/envio/'
	});
	$scope.currentPedidoId = $routeParams.pedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
		$scope.navegacion.actual.display = $scope.pedido.nombre;
	}, function(errResponse) {
	});
}]);

envioModule.controller('tikal.modules.Envio.subPedido.PedidoDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
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
		display: 'Envíos',
		link: '/envio/'
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
			link: '/envio/' + data.id
		});
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.pedido = data;
		$scope.navegacion.actual.display = $scope.pedido.puntoEntrega.nombreCorto;
	}, function(errResponse) {
	});
}]);

envioModule.controller('tikal.modules.Envio.EnvioDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
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
		display: 'Envíos',
		link: '/envio/'
	});
	$scope.currentPedidoId = $routeParams.pedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.navegacion.previos.push({
			display: data.nombre,
			link: '/envio/' + data.id
		});
		$scope.pedido = data;
	}, function(errResponse) {
	});
	$scope.envioDao.get({envioId:$routeParams.envioId}).$promise.then(function(data) {
		$scope.envio = data;
		$scope.navegacion.actual.display = $scope.envio.name;
	}, function(errResponse) {
	});
}]);

pedidoModule.controller('tikal.modules.Envio.PedidoContactoSeleccionCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'CONTACTO_REMOTE_ADDRESS', 
	'$location', '$uibModal', '$window','$log',
  function ($scope, $resource, $routeParams, config, configContacto, $location, $uibModal, $window, $log) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Envíos',
		link: '/envio/'
	});
	$scope.currentPedidoId = $routeParams.pedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{pedidoId:$routeParams.pedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoDao.get().$promise.then(function(data) {
		$scope.navegacion.previos.push({
			display: data.nombre,
			link: '/envio/' + data.id
		});
		$scope.pedido = data;
		$scope.envioDao.get({envioId:$routeParams.envioId}).$promise.then(function(data) {
			$scope.navegacion.previos.push({
				display: data.name,
				link: '/envio/' + $routeParams.pedidoId + '/envio/' + data.id
			});
			$scope.envio = data;
		}, function(errResponse) {
		});
	}, function(errResponse) {
	});
	$scope.navegacion.actual.display = 'Seleccion Transportista';
	$scope.group="Transportistas";
	$scope.nuevoActionName="Transportista";
	$scope.hiddeNew = true;
	
	var contactoDao = $resource(configContacto.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'query':  {method:'GET', isArray:false, params:{group:'transportista'}}
	});
	contactoDao.query().$promise.then(function(data) {
		$scope.contactos = data;
	}, function(errResponse) {

	});
	$scope.verContacto = function(contactoId) {
		if (!$scope.actionLoading) {
			var tmpModelo = angular.copy($scope.envio);
			tmpModelo.idTransportista = contactoId;
			delete tmpModelo['pedidoId'];
			$scope.actionLoading = true;
			$scope.envioDao.save({envioId:tmpModelo.id}, tmpModelo).$promise.then(
				function(data) {
					$scope.actionLoading = false;
					$location.path('/envio/' + $routeParams.pedidoId + '/envio/' + $routeParams.envioId);
				}, function(errResponse) {
					$scope.actionLoading = false;
			});
		}
	};
}]);

envioModule.controller('tikal.modules.Envio.subPedido.EnvioDetailCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$location', 
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
		display: 'Envíos',
		link: '/envio/'
	});
	$scope.pedidoRaizId = $routeParams.pedidoId;
	$scope.currentPedidoId = $routeParams.subPedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId, subPedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{pedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao.get({pedidoId:$routeParams.pedidoId}).$promise.then(function (data) {
		$scope.navegacion.previos.push({
			display: data.nombre,
			link: '/envio/' + data.id
		});
		$scope.pedidoDao.get().$promise.then(function(subData) {
			$scope.navegacion.previos.push({
				display: subData.puntoEntrega.nombreCorto,
				link: '/envio/' + data.id + '/subPedido/' + subData.id
			});
			$scope.pedido = subData;
		}, function(errResponse) {
		});
	});
	$scope.envioDao.get({envioId:$routeParams.envioId}).$promise.then(function(data) {
		$scope.envio = data;
		$scope.navegacion.actual.display = $scope.envio.name;
	}, function(errResponse) {
	});
}]);

pedidoModule.controller('tikal.modules.Envio.subPedido.PedidoContactoSeleccionCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', 'CONTACTO_REMOTE_ADDRESS', 
	'$location', '$uibModal', '$window','$log',
  function ($scope, $resource, $routeParams, config, configContacto, $location, $uibModal, $window, $log) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	};
	$scope.navegacion.previos.push({
		display: 'Envíos',
		link: '/envio/'
	});
	$scope.pedidoRaizId = $routeParams.pedidoId;
	$scope.currentPedidoId = $routeParams.subPedidoId;
	$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$routeParams.pedidoId, subPedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao = $resource(config.address + '/api/pedido/raiz/:pedidoId',{}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{pedidoId:$routeParams.subPedidoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.pedidoRaizDao.get({pedidoId:$routeParams.pedidoId}).$promise.then(function (data) {
		$scope.navegacion.previos.push({
			display: data.nombre,
			link: '/envio/' + data.id
		});
		$scope.pedidoDao.get().$promise.then(function(subData) {
			$scope.navegacion.previos.push({
				display: subData.puntoEntrega.nombreCorto,
				link: '/envio/' + data.id + '/subPedido/' + subData.id
			});
			$scope.pedido = subData;
			$scope.envioDao.get({envioId:$routeParams.envioId}).$promise.then(function(data) {
				$scope.navegacion.previos.push({
					display: data.name,
					link: '/envio/' + $routeParams.pedidoId + '/subPedido/' +  $routeParams.subPedidoId + '/envio/' + data.id
				});
				$scope.envio = data;
			}, function(errResponse) {
			});
		}, function(errResponse) {
		});
	});
	$scope.navegacion.actual.display = 'Seleccion Transportista';
	$scope.group="Transportistas";
	$scope.nuevoActionName="Transportista";
	$scope.hiddeNew = true;
	
	var contactoDao = $resource(configContacto.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'query':  {method:'GET', isArray:false, params:{group:'transportista'}}
	});
	contactoDao.query().$promise.then(function(data) {
		$scope.contactos = data;
	}, function(errResponse) {

	});
	$scope.verContacto = function(contactoId) {
		if (!$scope.actionLoading) {
			var tmpModelo = angular.copy($scope.envio);
			delete tmpModelo['pedidoId'];
			tmpModelo.idTransportista = contactoId;
			$scope.actionLoading = true;
			$scope.envioDao.save({envioId:tmpModelo.id}, tmpModelo).$promise.then(
				function(data) {
					$scope.actionLoading = false;
					$location.path('/envio/' + $routeParams.pedidoId + '/subPedido/' +  $routeParams.subPedidoId + '/envio/' + $routeParams.envioId);
				}, function(errResponse) {
					$scope.actionLoading = false;
			});
		}
	};
}]);

fabricacionModule.directive('envioList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/envio/envio-list.html',
	scope: {
	  pedido: '=pedido'
    },
	controller: ['$scope', '$resource', '$location', 'PRODUCCION_REMOTE_ADDRESS', 'CONTACTO_REMOTE_ADDRESS', '$uibModal', '$log', function($scope, $resource, $location, config, configContacto, $uibModal, $log) {
		$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{}, {
			'get':  {method:'GET', isArray:false, params:{}}
		});
		$scope.contactoDao = $resource(configContacto.address + '/api/customer/:contactId',{}, {
			'get':  {method:'GET', isArray:false, params:{group:'transportista'}}
		});
		var envioLoadCount = -1;
		$scope.fillNames = function() {
			angular.forEach($scope.envios, function(envio, i) {
				angular.forEach($scope.transportistas, function(transportista, j) {
					if (envio.idTransportista == transportista.id) {
						envio.transportista = {
							nombre:transportista.name.name
						};
					}
				});
			});
		};
		$scope.loadEnvios = function() {
			envioLoadCount = 2;
			$scope.contactoDao.get({pedidoId:$scope.pedido.id}).$promise.then(function(data) {
				angular.forEach(data.items, function(envio, i) {
					envio.transportista = {
						nombre:''
					};
				});
				$scope.transportistas = data.items;
				envioLoadCount = envioLoadCount -1;
				if (envioLoadCount == 0) {
					$scope.fillNames();
				}
			}, function(errResponse) {
			});
			$scope.envioDao.get({pedidoId:$scope.pedido.id}).$promise.then(function(data) {
				$scope.envios = data.items;
				envioLoadCount = envioLoadCount -1;
				if (envioLoadCount == 0) {
					$scope.fillNames();
				}
			}, function(errResponse) {
			});
		};
		$scope.loadEnvios();
		$scope.agregarEnvio = function(size) {
			var newModel = {
				name:'',
				status:'En preparación'
			};
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'view/envio/envio-nuevo.html',
				controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
				backdrop: 'static',
				size: size,
				resolve: {
					modelo: function () {
						return newModel;
					}
				}
			});
			modalInstance.result.then(function (modelo) {
				$scope.loadEnvios();
			});
			newModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = {
					type:'EnvioTransient',
					name:$modalScope.modelo.name,
					status:$modalScope.modelo.status
				};
				$modalScope.actionLoading = true;
				$scope.envioDao.save({pedidoId:$scope.pedido.id}, tmpModel).$promise.then(function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, function(errResponse) {
					$modalScope.actionLoading = false;
				});
			};
		};
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
		$scope.getCurrentPath = function() {
			return $location.path();
		};
		$scope.borrarEnvio = function(envio, size) {
			var dialogModel = {
				message: '¿Esta seguro de querer borrar el envío?',
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
				$scope.loadEnvios();
			});
			dialogModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				$scope.envioDao.delete({pedidoId:$scope.pedido.id, envioId:envio.id}).$promise.then(function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, function(errResponse) {
					$modalScope.actionLoading = false;
				});
			};
		};
	}]
  };
});

fabricacionModule.directive('envioSubPedidoList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/envio/subPedido-list.html',
	scope: {
	  pedido: '=pedido'
    },
	controller: ['$scope', '$resource', '$location', 'PRODUCCION_REMOTE_ADDRESS', '$log', function($scope, $resource, $location, config, $log) {
		$scope.pedidoDao = $resource(config.address + '/api/pedido/raiz/:pedidoId/subPedido/:subPedidoId',{pedidoId:$scope.pedido.id}, {
			'get':  {method:'GET', isArray:false, params:{}}
		});
		$scope.pedidoDao.get().$promise.then(function(data) {
			data.items.sort(function(a,b){
				return a.puntoEntrega.nombreCorto.localeCompare(b.puntoEntrega.nombreCorto);
			});
			$scope.subPedidos = data.items;
		}, function(errResponse) {
		});
		$scope.getCurrentPath = function() {
			return $location.path();
		};
	}]
  };
});

fabricacionModule.directive('envioEdicion', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/envio/envio-edicion.html',
	scope: {
	  pedido: '=pedido',
	  modelo: '=envio'
    },
	controller: ['$scope', '$resource', '$routeParams', '$location', 'PRODUCCION_REMOTE_ADDRESS', 'contactoRaizDao', '$uibModal', '$log', 
	function($scope, $resource, $routeParams, $location, config, contactoRaizDao, $uibModal, $log) {
		$scope.envioDao = $resource(config.address + '/api/pedido/:pedidoId/envio/:envioId',{}, {
			'get':  {method:'GET', isArray:false, params:{}}
		});
		$scope.$watch('modelo', function(newValue, oldValue) {
			if ($scope.modelo) {
				//$log.info($scope.modelo);
				if ($scope.modelo.idTransportista) {
					contactoRaizDao.get({contactId:$scope.modelo.idTransportista}).$promise.then(function(data) {
						$scope.modelo.transportista = data;
					}, function(errResponse) {
					});
				}
			}
		});
		$scope.verTransportista = function() {
			$location.path($location.path() + '/transportista/' + $scope.modelo.transportista.id);
		};
		$scope.seleccionarTransportista = function() {
			$location.path($location.path() + '/seleccion/transportista/');
		};
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
		$scope.editarNombre = function(size) {
			var capturaModel = {
				envio:$scope.modelo
			};
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'view/envio/captura-nombre-envio.html',
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
				$scope.modelo.name = modelo.name;
			});
			capturaModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = angular.copy($modalScope.modelo.envio);
				delete tmpModel['pedidoId'];
				delete tmpModel['salidas'];
				delete tmpModel['transportista'];
				$scope.envioDao.save({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}, tmpModel).$promise.then(
					function(data) {
						$modalScope.actionLoading = false;
						modalInstance.close(tmpModel);
					},
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			};
		};
		$scope.editarSalida = function(size) {
			var fechaSalida = null;
			if ($scope.modelo.fechaSalida) {
				fechaSalida = new Date($scope.modelo.fechaSalida.split('+')[0]);
			}
			var capturaModel = {
				fechaSalida:fechaSalida,
				formatoFecha:$scope.formatDP,
				opened:false,
				open: function(modelo, event) {
					modelo.opened = true;
				},
				envio:$scope.modelo
			};
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'view/envio/captura-salida-envio.html',
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
				$scope.modelo.fechaSalida = modelo.fechaSalida;
				$scope.modelo.status = modelo.status;
			});
			capturaModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = angular.copy($modalScope.modelo.envio);
				tmpModel.fechaSalida = $modalScope.modelo.fechaSalida;
				if (tmpModel.fechaEntrega) {
					tmpModel.status = 'Entregado';
				} else {
					if (tmpModel.fechaSalida) {
						tmpModel.status = 'En transito';
					} else {
						tmpModel.status = 'En preparación';
					}
				}
				delete tmpModel['pedidoId'];
				delete tmpModel['salidas'];
				delete tmpModel['transportista'];
				$scope.envioDao.save({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}, tmpModel).$promise.then(
					function(data) {
						$scope.envioDao.get({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}).$promise.then(
							function(reload) {
								$modalScope.actionLoading = false;
								modalInstance.close(reload);
							},
							function(errResponse) {
								$modalScope.actionLoading = false;
						});
					},
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			};
		};
		$scope.borrarSalida = function(size) {
			var dialogModel = {
				message: '¿Esta seguro de querer borrar la fecha de salida?',
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
				$scope.modelo.fechaSalida = null;
				$scope.modelo.status = modelo.status;
			});
			dialogModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = angular.copy($scope.modelo);
				tmpModel.fechaSalida = null;
				if (tmpModel.fechaEntrega) {
					tmpModel.status = 'Entregado';
				} else {
					if (tmpModel.fechaSalida) {
						tmpModel.status = 'En transito';
					} else {
						tmpModel.status = 'En preparación';
					}
				}
				delete tmpModel['pedidoId']
				delete tmpModel['salidas']
				delete tmpModel['transportista'];
				delete tmpModel['fechaSalida'];
				$scope.envioDao.save({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}, tmpModel).$promise.then(
					function(data) {
						$modalScope.actionLoading = false;
						modalInstance.close(tmpModel);
					},
					function(errResponse) {
						$modalScope.actionLoading = false;
				});
			};
		};
		$scope.editarEntrega = function(size) {
			var fechaEntrega = null;
			if ($scope.modelo.fechaEntrega) {
				fechaEntrega = new Date($scope.modelo.fechaEntrega.split('+')[0]);
			}
			var capturaModel = {
				fechaEntrega:fechaEntrega,
				formatoFecha:$scope.formatDP,
				opened:false,
				open: function(modelo, event) {
					modelo.opened = true;
				},
				envio:$scope.modelo
			};
			var modalInstance = $uibModal.open({
				animation: true,
				templateUrl: 'view/envio/captura-entrega-envio.html',
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
				$scope.modelo.fechaEntrega = modelo.fechaEntrega;
				$scope.modelo.status = modelo.status;
			});
			capturaModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = angular.copy($modalScope.modelo.envio);
				tmpModel.fechaEntrega = $modalScope.modelo.fechaEntrega;
				if (tmpModel.fechaEntrega) {
					tmpModel.status = 'Entregado';
				} else {
					if (tmpModel.fechaSalida) {
						tmpModel.status = 'En transito';
					} else {
						tmpModel.status = 'En preparación';
					}
				}
				delete tmpModel['pedidoId']
				delete tmpModel['salidas']
				delete tmpModel['transportista'];
				$scope.envioDao.save({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}, tmpModel).$promise.then(
					function(data) {
						$scope.envioDao.get({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}).$promise.then(
							function(reload) {
								$modalScope.actionLoading = false;
								modalInstance.close(reload);
							},
							function(errResponse) {
								$modalScope.actionLoading = false;
						});
					},
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			};
		};
		$scope.borrarEntrega = function(size) {
			var dialogModel = {
				message: '¿Esta seguro de querer borrar la fecha de entrega?',
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
				$scope.modelo.fechaEntrega = null;
				$scope.modelo.status = modelo.status;
			});
			dialogModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				var tmpModel = angular.copy($scope.modelo);
				tmpModel.fechaEntrega = null;
				if (tmpModel.fechaEntrega) {
					tmpModel.status = 'Entregado';
				} else {
					if (tmpModel.fechaSalida) {
						tmpModel.status = 'En transito';
					} else {
						tmpModel.status = 'En preparación';
					}
				}
				delete tmpModel['pedidoId']
				delete tmpModel['salidas']
				delete tmpModel['transportista'];
				delete tmpModel['fechaEntrega'];
				$scope.envioDao.save({pedidoId:$scope.pedido.id, envioId:$routeParams.envioId}, tmpModel).$promise.then(
					function(data) {
						$modalScope.actionLoading = false;
						modalInstance.close(tmpModel);
					},
					function(errResponse) {
						$modalScope.actionLoading = false;
				});
			};
		};
	}]
  };
});

fabricacionModule.directive('envioInventario', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/envio/inventario-envio.html',
	scope: {
		envio: '=envio'
    },
	controller: ['$scope', '$resource', '$location', 'PRODUCCION_REMOTE_ADDRESS', 'CONTACTO_REMOTE_ADDRESS', '$uibModal', '$log', function($scope, $resource, $location, config, configContacto, $uibModal, $log) {
		$scope.$watch('envio', function(newValue, oldValue) {
			if ($scope.envio) {
				$scope.loadEntradas($scope.envio.pedidoId);
			}
		});
		$scope.salidaDao = $resource(config.address + '/api/pedido/:pedidoId/almacen/salida/:salidaId',{}, {
			'query':  {method:'GET', isArray:false}
		});
		$scope.loadEntradas = function(pedidoId) {
			$scope.salidaDao.query({pedidoId:pedidoId}).$promise.then(function(data) {
				//ordena las entradas
				data.items.sort(function(a,b){
					var objA;
					var objB;
					if (a.type == 'SalidaIntermediario') {
						objA = a.producto.datosGenerales;
					} else {
						objA = a.linea.datosGenerales;
					}
					if (b.type == 'SalidaIntermediario') {
						objB = b.producto.datosGenerales;
					} else {
						objB = b.linea.datosGenerales;
					}
					return objA.nombre.localeCompare(objB.nombre);
				});
				angular.forEach(data.items, function(salida, key) {
					if (salida.type == 'GrupoRegistroAlmacen') {
						salida.registros.sort(function(a,b){
							return a.producto.talla.localeCompare(b.producto.talla);
						});
					}
				});
				//junta las salidas
				var salidasTmp = [];
				var salidaAnterior = null;
				angular.forEach(data.items, function(salida, i) {
					if (salida.type == 'GrupoRegistroAlmacen') {
						var registrosTmp = [];
						var registroAnterior = null;
						var totalGrupo = 0;
						angular.forEach(salida.registros, function(registro, j) {
							if ($scope.envio.id == registro.referenciaEnvio) {
								$log.info(registro.cantidad);
								totalGrupo = totalGrupo + registro.cantidad;
								if (registroAnterior && registroAnterior.producto.id == registro.producto.id && registroAnterior.producto.catalogoId == registro.producto.catalogoId) {
									registroAnterior.cantidad = registroAnterior.cantidad + registro.cantidad;
								} else {
									registrosTmp.push(registro);
									registroAnterior = registro;
								}
							}
						});
						salida.cantidad = totalGrupo;
						if (registrosTmp.length > 0) {
							salida.registros = registrosTmp;
							salidasTmp.push(salida);
						}
						salidaAnterior = null;
					} else {
						if ($scope.envio.id == salida.referenciaEnvio) {
							if (salidaAnterior && salidaAnterior.producto.id == salida.producto.id && salidaAnterior.producto.catalogoId == salida.producto.catalogoId) {
								salidaAnterior.cantidad = salidaAnterior.cantidad + salida.cantidad;
							} else {
								salidasTmp.push(salida);
								salidaAnterior = salida;
							}
						}
					}
				});
				$scope.envio.salidas = salidasTmp;
			}, function(errResponse) {
			});
		};
		$scope.hideShowSub = function(registro) {
			if (registro.showSub) {
				registro.showSub = false;
			} else {
				angular.forEach($scope.envio.salidas, function(value, key) {
					if (value.type=='GrupoRegistroAlmacen') {
						value.showSub = false;
					}
				});
				registro.showSub = true;
			}
		};
	}]
  };
});
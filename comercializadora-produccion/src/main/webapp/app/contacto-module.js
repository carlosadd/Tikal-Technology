'use strict';

var contactoModule = angular.module('tikal.modules.Contacto', ['ngRoute','ngResource','angular-ladda']);

contactoModule.constant('CONTACTO_REMOTE_ADDRESS', {
	address: 'https://tolumex-clients.appspot.com',
});

contactoModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/contacto/:grupoId', {templateUrl: 'view/contacto/contacto-list.html', controller: 'tikal.modules.Contacto.ContactoListCtrl'});
	$routeProvider.when('/contacto/:grupoId/:contactId', {templateUrl: 'view/contacto/contacto-detail.html', controller: 'tikal.modules.Contacto.ContactoCtrl'});
	$routeProvider.when('/pedido/:pedidoId/cliente/:contactId', {templateUrl: 'view/contacto/contacto-detail.html', controller: 'tikal.modules.Contacto.ContactoCtrl'});
	$routeProvider.when('/contacto/:grupoId/:contactId/asociado/:asociadoId', {templateUrl: 'view/contacto/asociado-detail.html', controller: 'tikal.modules.Contacto.AsociadoCtrl'});
	$routeProvider.when('/pedido/:pedidoId/cliente/:contactId/asociado/:asociadoId', {templateUrl: 'view/contacto/asociado-detail.html', controller: 'tikal.modules.Contacto.AsociadoCtrl'});
});

contactoModule.controller('tikal.modules.Contacto.ContactoListCtrl', ['$scope', '$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	if ($routeParams.grupoId == 'proveedor') {
		$scope.group="Proveedores";
		$scope.nuevoActionName="Proveedor";
	}
	if ($routeParams.grupoId == 'transportista') {
		$scope.group="Transportistas";
		$scope.nuevoActionName="Transportista";
	}
	if ($routeParams.grupoId == 'cliente') {
		$scope.group="Clientes";
		$scope.nuevoActionName="Cliente";
	}
	$scope.navegacion.actual.display = $scope.group;
	var contactoDao = $resource(config.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'query':  {method:'GET', isArray:false, params:{group:$routeParams.grupoId}}
	});
	contactoDao.query().$promise.then(function(data) {
		$scope.contactos = data;
	}, function(errResponse) {

	});
	$scope.verContacto = function(contactoId) {
	  $location.path($location.path() + '/' + contactoId);
	};
	
	$scope.nuevoContacto = function(size) {
		$scope.modeloNuevoContacto = {
			type:"ClienteMx",
			name: {
				type: "OrganizationName",
				name: ""
			},
			group:{
				name:$routeParams.grupoId
			},
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				contactoDao.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					$location.path($location.path() + '/' + newId);
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

contactoModule.controller('tikal.modules.Contacto.select.proveedor', ['$scope', '$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	var contactoDao = $resource(config.address + '/api/customer/:contactId',{}, {
		'query':  {method:'GET', isArray:false, params:{group:'proveedor'}}
	});
	contactoDao.query().$promise.then(function(data) {
		$scope.contactos = data.items;
	}, function(errResponse) {

	});
	
}]);

contactoModule.controller('tikal.modules.Contacto.ContactoCtrl', ['$scope', '$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', '$log', '$uibModal', '$window',
  function ($scope, $resource, $routeParams, config, $log, $uibModal, $window) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	if ($routeParams.pedidoId) {
		$scope.navegacion.previos.push({
			display: 'Pedidos',
			link: '/pedido/'
		});
		$scope.navegacion.previos.push({
			display: $window.sessionStorage.getItem('nombrePedidoActual'),
			link: '/pedido/' + $routeParams.pedidoId
		});
	} else {
		if ($routeParams.grupoId == 'proveedor') {
			$scope.group="Proveedores";
		}
		if ($routeParams.grupoId == 'transportista') {
			$scope.group="Transportistas";
		}
		if ($routeParams.grupoId == 'cliente') {
			$scope.group="Clientes";
		}
		$scope.navegacion.previos.push({
			display: $scope.group,
			link: '/contacto/' + $routeParams.grupoId
		});
	}
	$scope.contactoRaizDao = $resource(config.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.contactoRaizDao.get().$promise.then(function(data) {
		$scope.contactoRaiz = data;
		$scope.contacto = $scope.contactoRaiz.primaryContact;
		$scope.navegacion.actual.display = $scope.contactoRaiz.name.name;
	}, function(errResponse) {
	
	});
	$scope.contactoDao = {
		save: function(data) {
			var tmpModelo = angular.copy($scope.contactoRaiz);
			tmpModelo.primaryContact = data;
			return $scope.contactoRaizDao.save(tmpModelo);
		}
	};
	$scope.editarHeader = function(size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/contacto/contacto-edicion-header.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.contactoRaiz;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.contactoRaiz = modelo;
			$scope.contacto = $scope.contactoRaiz.primaryContact;
			$scope.navegacion.actual.display = $scope.contactoRaiz.name.name;
		});
		$scope.contactoRaiz.command = function($modalScope) {
			$modalScope.actionLoading = true;
			if ($modalScope.modelo.primaryContact.name && $modalScope.modelo.primaryContact.name.name) {
				$modalScope.modelo.primaryContact.name.type = 'OrganizationName';
			} else {
				delete $modalScope.modelo.primaryContact.name;
			}
			$scope.contactoRaizDao.save($modalScope.modelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
}]);

contactoModule.controller('tikal.modules.Contacto.AsociadoListCtrl', ['$scope', '$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', 
	'$location', '$log', '$uibModal', 'tikal.modules.Util.Json', 
  function ($scope, $resource, $routeParams, config, $location, $log, $uibModal, jsonUtil) {
	$scope.asociadoDao = $resource(config.address + '/api/customer/:contactId/contact',{contactId:$routeParams.contactId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.asociadoDao.get().$promise.then(function(data) {
		$scope.asociados = data;
	}, function(errResponse) {

	});
	$scope.verAsociado = function(asociadoId) {
	  $location.path($location.path() + '/asociado/' + asociadoId);
	};
	$scope.nuevoAsociado = function(size) {
		$scope.modeloNuevoAsociado = {
			type:"Associate",
			name: {
				type: "OrganizationName",
				name: ""
			},
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				jsonUtil.clean($modalScope.modelo);
				$scope.asociadoDao.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					$location.path($location.path() + '/asociado/' + newId);
				}).$promise.then(
					function(data) {}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/contacto/asociado-nuevo.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevoAsociado;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//$scope.modelo.primaryContact.address[index] = angular.copy(modelo);
		});
	};
}]);

contactoModule.controller('tikal.modules.Contacto.AsociadoCtrl', ['$scope', '$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', 
	'$location', '$log', '$uibModal', 'tikal.modules.Util.Json', '$window',
  function ($scope, $resource, $routeParams, config, $location, $log, $uibModal, jsonUtil, $window) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	if ($routeParams.pedidoId) {
		$scope.navegacion.previos.push({
			display: 'Pedidos',
			link: '/pedido/'
		});
		$scope.navegacion.previos.push({
			display: $window.sessionStorage.getItem('nombrePedidoActual'),
			link: '/pedido/' + $routeParams.pedidoId
		});
		$scope.navegacion.previos.push({
			display: '',
			link: '/pedido/' + $routeParams.pedidoId + '/cliente/' + $routeParams.contactId
		});
	} else {
		if ($routeParams.grupoId == 'proveedor') {
			$scope.group="Proveedores";
		}
		if ($routeParams.grupoId == 'transportista') {
			$scope.group="Transportistas";
		}
		if ($routeParams.grupoId == 'cliente') {
			$scope.group="Clientes";
		}
		$scope.navegacion.previos.push({
			display: $scope.group,
			link: '/contacto/' + $routeParams.grupoId
		});
		$scope.navegacion.previos.push({
			display: '',
			link: '/contacto/' + $routeParams.grupoId + '/' + $routeParams.contactId
		});
	}
	$scope.contactoRaizDao = $resource(config.address + '/api/customer/:contactId',{contactId:$routeParams.contactId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.contactoRaizDao.get().$promise.then(function(data) {
		$scope.contactoRaiz = data;
		if ($routeParams.pedidoId) {
			$scope.navegacion.previos[2].display = $scope.contactoRaiz.name.name;
		} else {
			$scope.navegacion.previos[1].display = $scope.contactoRaiz.name.name;
		}
	}, function(errResponse) {
	
	});
	$scope.contactoDao = $resource(config.address + '/api/customer/:contactId/contact/:asociadoId',{contactId:$routeParams.contactId, asociadoId:$routeParams.asociadoId},{
		'get':{method:'GET', isArray:false, params:{}}
	});
	$scope.contactoDao.get().$promise.then(function(data) {
		$scope.contacto = data;
		$scope.navegacion.actual.display = $scope.contacto.name.name;
	}, function(errResponse) {

	});
	$scope.editarHeader = function(size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/contacto/asociado-edicion-header.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.contacto;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.contacto = modelo;
			$scope.navegacion.actual.display = $scope.contacto.name.name;
		});
		$scope.contacto.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.contactoDao.save($modalScope.modelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
}]);

contactoModule.controller('tikal.modules.Contacto.address.EditCtrl', ['$scope',
  function ($scope) {
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
	$scope.configDetalle = {
		'MexicoAddress':{
			template:'view/contacto/address-edit.html',
			getNuevo: function(){
				var nuevo = {
					type:'MexicoAddress'
				};
				return nuevo;
			},
			addToModel: function(target, data) {
				target.address.push(data);
			},
			getModel: function(target, pos) {
				return target.address[pos];
			},
			updateModel: function(target, pos, data) {
				target.address[pos] = data;
			},
			deleteModel: function(target, pos) {
				target.address.splice(pos, 1);
			},
			getDialogModel: function() {
				var dialogModel = {
					message: '¿Esta seguro de querer borrar la dirección?',
					type:'warning'
				}
				return dialogModel;
			}
		}
	};
}]);

contactoModule.controller('tikal.modules.Contacto.phone.EditCtrl', ['$scope',
  function ($scope) {
	$scope.configDetalle = {
		'MexicoPhoneNumber':{
			template:'view/contacto/telefono-edit.html',
			getNuevo: function(){
				var nuevo = {
					type:'MexicoPhoneNumber'
				};
				return nuevo;
			},
			addToModel: function(target, data) {
				target.phoneNumber.push(data);
			},
			getModel: function(target, pos) {
				return target.phoneNumber[pos];
			},
			updateModel: function(target, pos, data) {
				target.phoneNumber[pos] = data;
			},
			deleteModel: function(target, pos) {
				target.phoneNumber.splice(pos, 1);
			},
			getDialogModel: function() {
				var dialogModel = {
					message: '¿Esta seguro de querer borrar el telefono?',
					type:'warning'
				}
				return dialogModel;
			}
		}
	};
}]);

contactoModule.controller('tikal.modules.Contacto.media.EditCtrl', ['$scope',
  function ($scope) {
	$scope.configDetalle = {
		'Email':{
			template:'view/contacto/email-edit.html',
			getNuevo: function(){
				var nuevo = {
					type:'Email'
				};
				return nuevo;
			},
			addToModel: function(target, data) {
				target.mediaContact.push(data);
			},
			getModel: function(target, pos) {
				return target.mediaContact[pos];
			},
			updateModel: function(target, pos, data) {
				target.mediaContact[pos] = data;
			},
			deleteModel: function(target, pos) {
				target.mediaContact.splice(pos, 1);
			},
			getDialogModel: function() {
				var dialogModel = {
					message: '¿Esta seguro de querer borrar el e-mail?',
					type:'warning'
				}
				return dialogModel;
			}
		},
		'SocialNetwork':{
			template:'view/contacto/socialnetwork-edit.html',
			getNuevo: function(){
				var nuevo = {
					type:'SocialNetwork'
				};
				return nuevo;
			},
			addToModel: function(target, data) {
				target.mediaContact.push(data);
			},
			getModel: function(target, pos) {
				return target.mediaContact[pos];
			},
			updateModel: function(target, pos, data) {
				target.mediaContact[pos] = data;
			},
			deleteModel: function(target, pos) {
				target.mediaContact.splice(pos, 1);
			},
			getDialogModel: function() {
				var dialogModel = {
					message: '¿Esta seguro de querer borrar la pagina?',
					type:'warning'
				}
				return dialogModel;
			}
		}
	};
}]);

contactoModule.controller('tikal.modules.Contacto.detail.EditCtrl', ['$scope', '$log', '$uibModal', 'tikal.modules.Util.Json', 
  function ($scope, $log, $uibModal, jsonUtil) {
	$scope.nuevoDetalle = function(type, size) {
		var nuevo = $scope.configDetalle[type].getNuevo();
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: $scope.configDetalle[type].template,
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return nuevo;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//si todo sale bien se modifica el modelo real
			$scope.configDetalle[type].addToModel($scope.contacto, modelo);
		});
		nuevo.command = function($modalScope) {
			$modalScope.actionLoading = true;
			//se modifica un modelo temporal por si algo sale mal
			jsonUtil.clean($modalScope.modelo);
			var tmpModelo = angular.copy($scope.contacto);
			$scope.configDetalle[type].addToModel(tmpModelo, $modalScope.modelo);
			$scope.contactoDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.editarDetalle = function(type, index, size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: $scope.configDetalle[type].template,
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.configDetalle[type].getModel($scope.contacto, index);
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//si todo sale bien se modifica el modelo real
			$scope.configDetalle[type].updateModel($scope.contacto, index, modelo);
		});
		$scope.configDetalle[type].getModel($scope.contacto, index).command = function($modalScope) {
			$modalScope.actionLoading = true;
			//se modifica un modelo temporal por si algo sale mal
			jsonUtil.clean($modalScope.modelo);
			var tmpModelo = angular.copy($scope.contacto);
			$scope.configDetalle[type].updateModel(tmpModelo, index, $modalScope.modelo);
			$scope.contactoDao.save(tmpModelo).$promise.then(
				function(data) {
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				},
				function(errResponse) {
					$modalScope.actionLoading = false;
			});	
		};
	};
	$scope.borrarDetalle = function(type, index, size) {
		//$scope.$emit('inicia-submit','borrar');
		var dialogModel = $scope.configDetalle[type].getDialogModel();
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
			//si todo sale bien se modifica el modelo real
			$scope.configDetalle[type].deleteModel($scope.contacto, index);
		});
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			//se modifica un modelo temporal por si algo sale mal
			var tmpModelo = angular.copy($scope.contacto);
			$scope.configDetalle[type].deleteModel(tmpModelo, index);
			$scope.contactoDao.save(tmpModelo).$promise.then(
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

contactoModule.controller('tikal.modules.Contacto.ModalGenericEditCtrl',['$scope', '$modalInstance','modelo', '$log',
	function ($scope, $modalInstance, modelo, command, $log) {
	$scope.$emit('modal.show','');
	$scope.$on('modal.stack.now-closing', function(event, data) {
		$scope.$emit('modal.hide','');
	});
	$scope.alerts = [];
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	$scope.$on('ocurrio-error', function(event, data) {
		$scope.alerts.push({type: 'danger', msg: data});
	});
	$scope.$on('$routeChangeStart', function(event, data) {
		$modalInstance.dismiss('routeChangeStart');
	});
	$scope.modelo = {};
	$scope.modelo = angular.copy(modelo);
	$scope.ok = function () {
		if (modelo.command) {
			$scope.alerts = [];
			modelo.command($scope);
		} else {
			$modalInstance.close($scope.modelo);
		}
	};
	$scope.close = function() {
		$modalInstance.dismiss('close');
	};
	$scope.cancel = function () {
		$scope.alerts = [];
		$scope.modelo = angular.copy(modelo);
		$scope.editando = false;
	};
	$scope.iniciarEdicion = function () {
		$scope.alerts = [];
		$scope.editando = true;
	};
}]);

contactoModule.directive('patternXregexp', function($log) {
  return {
    require: 'ngModel',
	scope: {
		regexp: "=patternXregexp"
	},
    link: function($scope, $elm, $attrs, $ctrl) {
      $ctrl.$validators.patternXregexp = function(modelValue) {
		if ($ctrl.$isEmpty(modelValue)) {
			return true;
		}
		var unicodePunctuation = XRegExp($scope.regexp);
		return unicodePunctuation.test(modelValue);
      };
    }
  };
});

accountModule.directive('contactoNavegacion', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/contacto/contacto-navegacion.html'
  };
});

accountModule.directive('contactAddress', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/contacto/contacto-detail-address.html'
  };
});

accountModule.directive('contactPhoneNumber', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/contacto/contacto-detail-phone.html'
  };
});

accountModule.directive('contactMedia', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/contacto/contacto-detail-media.html'
  };
});

accountModule.directive('asociadoList', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/contacto/asociado-list.html'
  };
});

accountModule.directive('googleMapsAddress', function() {
  return {
    restrict: 'E',
	scope: {
      address: '=address'
    },
    templateUrl: 'view/contacto/google-maps-address.html',
	controller: function($scope) {
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
	}
  }
});

accountModule.factory('contactoRaizDao', ['$resource', '$routeParams', 'CONTACTO_REMOTE_ADDRESS', 
  function ($resource, $routeParams, config) {
    return $resource(config.address + '/api/customer/:contactId',{}, {
		'get': {method:'GET', isArray:false, params:{}},
		'query': {method:'GET', isArray:false, params:{}}
	});
}]);
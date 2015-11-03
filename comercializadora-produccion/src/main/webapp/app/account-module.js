'use strict';

var accountModule = angular.module('tikal.modules.Account', ['ngRoute','ngResource','angular-ladda','ngDialog', 'ngCookies']);

accountModule.constant('ACCOUNTS_REMOTE_ADDRESS', {
	address: 'https://tolumex-accounts.appspot.com',
});

accountModule.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
});

/*.constant('USER_ROLES', {
  all: '*',
  admin: 'admin',
  editor: 'editor',
  guest: 'guest'
})*/

accountModule.factory('tikal.modules.Account.authInterceptor', function ($rootScope, $q, $cookies, $log, AUTH_EVENTS) {
  return {
    request: function (config) {
      config.headers = config.headers || {};
	  var miSesion = $cookies.getObject('miSesion');
      if (miSesion) {
		//$log.info('con credenciales: ' + miSesion.token);
		config.headers.Authorization = 'Basic ' + miSesion.token;
      } else {
		//$log.info('sin credenciales!');
	  }
      return config;
    },
    responseError: function (response) {
		//$log.info('status response:' + response.status);
		if (response.status == 401) {
			//$log.info('No autorizado!');
			$rootScope.$broadcast(AUTH_EVENTS.notAuthorized, response.status);
		}
		return $q.reject(response);
    }
  };
});

accountModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/my/account', {templateUrl: 'view/account/account-preferencias.html', controller: 'tikal.modules.Account.PreferenciasCtrl'});
	$routeProvider.when('/account', {templateUrl: 'view/account/account-list.html', controller: 'tikal.modules.Account.ListAccountCtrl'});
	$routeProvider.when('/account/:accountId', {templateUrl: 'view/account/account-admin-edit.html', controller: 'tikal.modules.Account.AdminEditCtrl'});
	$routeProvider.when('/new/account', {templateUrl: 'view/account/account-creation.html', controller: 'tikal.modules.Account.CreateAccountCtrl'});
	/* CORS... */
	/* http://stackoverflow.com/questions/17289195/angularjs-post-data-to-external-rest-api */
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
	$httpProvider.interceptors.push('tikal.modules.Account.authInterceptor');
});

accountModule.controller('tikal.modules.Account.LoginCtrl', ['$scope', '$location', 'tikal.service.AuthService', '$log', 'tikal.modules.Account.local.Store', 'AUTH_EVENTS',
  function ($scope, $location, AuthService, $log, sesionStore, AUTH_EVENTS) {
	$scope.credentials = {
		user: '',
		password: '',
		otp: ''
	};
	$scope.miSesion = sesionStore.loadSession();
    $scope.login = function () {
		$scope.$emit('inicia-submit','login');
		$scope.loginLoading = true;
		AuthService.login($scope.credentials).$promise.then(function (loginData) {
			sesionStore.saveSession(loginData);
			$scope.miSesion = sesionStore.loadSession();
			//$log.info('login user: ' + $scope.miSesion.user);
			$scope.loginLoading = false;
		}, function (errResponse) {
			$scope.loginLoading = false;
			/*if (errResponse.status != 0) {
				$scope.$emit('ocurrio-error', errResponse.data.message[0]);
			}*/
		});
	};
	$scope.logout = function () {
		$location.path('');
		sesionStore.clearSession();
		$scope.miSesion = null;
		$scope.credentials = {
			user: '',
			password: '',
			otp: ''
		};
	};
	$scope.$on(AUTH_EVENTS.notAuthorized, function (event, data) {
		$scope.logout();
	});
}]);

accountModule.controller('tikal.modules.Account.ListAccountCtrl', ['$scope', '$resource', 'ACCOUNTS_REMOTE_ADDRESS', '$location', 'tikal.model.Accounts', 
  function ($scope, $resource, config, $location, accounts) {
	accounts.query().$promise.then(function (data) {
		$scope.accounts = data;
	}, function (errResponse) {
		//manejar error
	});	
	$scope.nuevaCuenta = function() {
		$location.path('/new/account');
	}
	$scope.verDetalle = function(accountId) {
		$location.path('/account/' + accountId);
	}
}]);

accountModule.controller('tikal.modules.Account.CreateAccountCtrl', ['$scope', '$resource', '$location', 'tikal.model.Accounts', 'tikal.model.Roles', '$log',
  function ($scope, $resource, $location, accounts, roles, $log) {
	$scope.resetModel = function(form) {
		if (form) {
			form.$setPristine();
			form.$setUntouched();
		}
		$scope.model = {
			user:'',
			password:{
				value:''
			},
			role:{
				value:''
			}
		};
		$scope.validacion = {
			confirmPassword: ''
		};
	};
	$scope.resetModel();
	roles.query().$promise.then(function (data) {
		$scope.roles = data;
	}, function (errResponse) {
		//manejar error
	});
	$scope.ok = function(form) {
		$scope.actionLoading = true;
		$scope.$emit('inicia-submit','ok');
		accounts.save($scope.model).$promise.then(function () {
			$scope.actionLoading = false;
			$scope.resetModel(form);
			$scope.$emit('operacion-exitosa', 'Se creo el nuevo usuario');
		}, function (errResponse) {
			$scope.actionLoading = false;
		});	
	};
	$scope.cancel = function() {
		$location.path('/account');
	};
}]);

accountModule.controller('tikal.modules.Account.AdminEditCtrl', ['$scope', '$resource', '$location', 'ACCOUNTS_REMOTE_ADDRESS', 
	'$routeParams', 'tikal.model.Account', 'tikal.model.Roles', '$log', 'ngDialog',
  function ($scope, $resource, $location, config, $routeParams, account, roles, $log, ngDialog) {
	$scope.ok = function(form) {
		$scope.saveLoading = true;
		$scope.$emit('inicia-submit','ok');
		var personalInfoJob = {
			id:'personalInfo',
			work: function() {
				var personalInfo = $resource(
					config.address + '/api/admin/accounts/:user/personalInfo',
					{user:$scope.model.user}, {}
				);
				personalInfo.save($scope.model.personalInfo).$promise.then(function () {
					personalInfoJob.sucess = true;
					personalInfoJob.finish = true;
					callEnd(personalInfoJob.id);
				}, function (errResponse) {
					personalInfoJob.finish = true;
					callEnd(personalInfoJob.id);
				});
			},
			sucess:false,
			finish:false
		};
		var roleJob = {
			id:'tipo',
			work: function() {
				var tipo = $resource(
					config.address + '/api/admin/accounts/:user/role',
					{user:$scope.model.user}, {}
				);
				tipo.save($scope.model.role).$promise.then(function () {
					roleJob.sucess = true;
					roleJob.finish = true;
					callEnd(roleJob.id);
				}, function (errResponse) {
					roleJob.finish = true;
					callEnd(roleJob.id);
				});
			},
			sucess:false,
			finish:false
		};
		var jobs = [];
		if (form.name.$dirty || form.email.$dirty) {
			personalInfoJob.started = true;
			jobs.push(personalInfoJob);
		}
		if (form.tipo.$dirty) {
			roleJob.started = true;
			jobs.push(roleJob);
		}
		var callEnd = function(idCall) {
			//$log.info(idCall + ':llamo!');
			var continuar = true;
			angular.forEach(jobs, function(value, key) {
				if (!value.finish) {
					//$log.info(idCall + ':aun no termina ' + value.id);
					continuar = false;
				}
			});
			if (continuar) {
				$scope.saveLoading = false;
				angular.forEach(jobs, function(value, key) {
					if (!value.sucess) {
						//$log.info(idCall + ':fallo' + value.id);
						continuar = false;
					}
				});
				if (continuar) {
					$scope.resetModel(form);
					//$log.info(idCall + ':generando evento!');
					$scope.$emit('operacion-exitosa', 'Se actualizo el usuario');
				}
			}
		};
		angular.forEach(jobs, function(value, key) {
			value.work();
		});
	};
	$scope.cancel = function() {
		$location.path('/account');
	};
	$scope.borrar = function() {
		$scope.$emit('inicia-submit','borrar');
		ngDialog.openConfirm({ 
			template: 'view/account/account-delete-dialog.html' ,
			cache: false,
		}).then(function (value) {
			$scope.deleteLoading = true;
			account.delete({user:$scope.model.user}).$promise.then(function (data) {
				$scope.deleteLoading = false;
				$location.path('/account');
			}, function (errResponse) {
				$scope.deleteLoading = false;
			});
		}, function (reason) {
			
		});
	};
	
	$scope.desactivar = function() {
		$scope.$emit('inicia-submit','desactivar');
		$scope.desactivarLoading = true;
		var status = $resource(
			config.address + '/api/admin/accounts/:user/status',
			{user:$scope.model.user}, {}
		);
		status.save({blocked:true}).$promise.then(function (data) {
			$scope.desactivarLoading = false;
			$scope.$emit('operacion-exitosa', 'Se desactivo el usuario');
			$scope.resetModel();
		}, function (errResponse) {
			$scope.desactivarLoading = false;
		});
	};
	$scope.activar = function() {
		$scope.$emit('inicia-submit','activar');
		$scope.desactivarLoading = true;
		var status = $resource(
			config.address + '/api/admin/accounts/:user/status',
			{user:$scope.model.user}, {}
		);
		status.save({blocked:false}).$promise.then(function (data) {
			$scope.desactivarLoading = false;
			$scope.$emit('operacion-exitosa', 'Se activo el usuario');
			$scope.resetModel();
		}, function (errResponse) {
			$scope.desactivarLoading = false;
		});
	};
	roles.query().$promise.then(function (data) {
		$scope.roles = data;
	}, function (errResponse) {
		//manejar error
	});
	$scope.resetModel = function(form) {
		if (form) {
			form.$setPristine();
			form.$setUntouched();
		}
		$scope.model = {
			user:$routeParams.accountId
		};
		account.get($scope.model).$promise.then(function (data) {
			$scope.model = data;
		}, function (errResponse) {
			//manejar error
		});
	};
	$scope.resetModel();
}]);

accountModule.controller('tikal.modules.Account.PreferenciasCtrl', ['$scope', '$resource', '$location', 'ACCOUNTS_REMOTE_ADDRESS', '$routeParams', '$log', 'ngDialog',
  function ($scope, $resource, $location, config, $routeParams, $log, ngDialog) {
	var account = $resource(config.address + '/api/my/account',{},{
	});
	var otpStatus = $resource(config.address + '/api/my/account/otp/status',{},{
	});
	var otpService = $resource(config.address + '/api/my/account/otp',{}, {
	});
	$scope.ok = function(form) {
		$scope.saveLoading = true;
		$scope.$emit('inicia-submit','ok');
		var personalInfoJob = {
			id:'personalInfo',
			work: function() {
				var personalInfo = $resource(config.address + '/api/my/account/personalInfo',{}, {
				});
				personalInfo.save($scope.model.personalInfo).$promise.then(function () {
					personalInfoJob.sucess = true;
					personalInfoJob.finish = true;
					callEnd(personalInfoJob.id);
				}, function (errResponse) {
					personalInfoJob.finish = true;
					callEnd(personalInfoJob.id);
				});
			},
			sucess:false,
			finish:false
		};
		var passwordJob = {
			id:'password',
			work: function() {
				var tipo = $resource(config.address + '/api/my/account/password',{},{
				});
				tipo.save($scope.model.password).$promise.then(function () {
					passwordJob.sucess = true;
					passwordJob.finish = true;
					callEnd(passwordJob.id);
				}, function (errResponse) {
					passwordJob.finish = true;
					callEnd(passwordJob.id);
				});
			},
			sucess:false,
			finish:false
		};
		var jobs = [];
		if (form.name.$dirty || form.email.$dirty) {
			personalInfoJob.started = true;
			jobs.push(personalInfoJob);
		}
		if (form.password.$dirty) {
			passwordJob.started = true;
			jobs.push(passwordJob);
		}
		var callEnd = function(idCall) {
			//$log.info(idCall + ':llamo!');
			var continuar = true;
			angular.forEach(jobs, function(value, key) {
				if (!value.finish) {
					//$log.info(idCall + ':aun no termina ' + value.id);
					continuar = false;
				}
			});
			if (continuar) {
				$scope.saveLoading = false;
				angular.forEach(jobs, function(value, key) {
					if (!value.sucess) {
						//$log.info(idCall + ':fallo' + value.id);
						continuar = false;
					}
				});
				if (continuar) {
					$scope.resetModel(form);
					//$log.info(idCall + ':generando evento!');
					$scope.$emit('operacion-exitosa', 'Se actualizo el usuario');
				}
			}
		};
		angular.forEach(jobs, function(value, key) {
			value.work();
		});
	};
	
	$scope.activarOtp = function() {
		$scope.$emit('inicia-submit','activarOtp');
		$scope.otpLoading = true;
		otpService.get().$promise.then(function (data) {
			$scope.otpLoading = false;
			$scope.$emit('operacion-exitosa', 'Se activo el otp');
			$scope.resetModel();
			$scope.otpSyncInfo = data;
		}, function (errResponse) {
			$scope.otpLoading = false;
		});
	};
	
	$scope.desactivarOtp = function() {
		$scope.$emit('inicia-submit','desactivarOtp');
		$scope.otpLoading = true;
		otpService.remove().$promise.then(function (data) {
			$scope.otpLoading = false;
			$scope.$emit('operacion-exitosa', 'Se desactivo el otp');
			$scope.resetModel();
		}, function (errResponse) {
			$scope.otpLoading = false;
		});
	};
	
	$scope.resetModel = function(form) {
		$scope.otpSyncInfo = null;
		if (form) {
			form.$setPristine();
			form.$setUntouched();
		}
		account.get().$promise.then(function (data) {
			$scope.model = data;
			$scope.model.password = {
				value:''
			};
			$scope.validacion = {
				confirmPassword: $scope.model.password.value
			};
		}, function (errResponse) {
			//manejar error
		});
		otpStatus.get().$promise.then(function(data) {
			$scope.otp = {
				status:data
			};
		}, function(errResponse) {
	
		});
	};
	$scope.resetModel();
}]);

accountModule.service('tikal.modules.Account.local.Store', ['$cookies', '$log', '$window',
  function ($cookies, $log, $window) {
	this.loadSession = function() {
		var miSesion = $cookies.getObject('miSesion');
		return miSesion;
	};
	this.saveSession = function(loginData) {
		var token = loginData.user + ':' + loginData.token;
		token = $window.btoa(token);
		var miSesion = {
			token:token,
			user:loginData.user,
			name:loginData.name,
			role:loginData.role,
			logged:true
		};
		$cookies.putObject('miSesion', miSesion);
	};
	this.clearSession = function() {
		$cookies.remove('miSesion');
	};
}]);

accountModule.directive('iguales', function($log) {
  return {
    require: 'ngModel',
	scope: {
		otherModelValue: "=iguales"
	},
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.iguales = function(modelValue) {
		if (!scope.otherModelValue) {
			return true;
		}
        if (modelValue == scope.otherModelValue) {
			return true;
        }
        return false;
      };
	  scope.$watch("otherModelValue", function() {
		ctrl.$validate();
	  });
    }
  };
});

accountModule.directive('accountLogin', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/account/account-login.html'
  };
});

accountModule.filter('AccountStatusFilter', function() {
    return function(input) {
        return input ? 'Bloqueado' : 'Activo';
    }
});

accountModule.factory('tikal.service.AuthService',['$resource', 'ACCOUNTS_REMOTE_ADDRESS', function ($resource, config) {
    return $resource(config.address + '/api/login', {}, {
        login: { method: 'POST', isArray: false }
    })
}]);

accountModule.factory('tikal.model.Accounts', ['$resource', 'ACCOUNTS_REMOTE_ADDRESS', function ($resource, config) {
	return $resource(config.address + '/api/admin/accounts', {}, {
		query: { method: 'GET', isArray: false }
	})
}]);

accountModule.factory('tikal.model.Account', ['$resource', 'ACCOUNTS_REMOTE_ADDRESS', function ($resource, config) {
	return $resource(config.address + '/api/admin/accounts/:user', {user:'@user'}, {
		
	})
}]);

accountModule.factory('tikal.model.Roles', ['$resource', 'ACCOUNTS_REMOTE_ADDRESS', function ($resource, config) {
	return $resource(config.address + '/api/admin/roles', {}, {
		query: { method: 'GET', isArray: false }
	})
}]);
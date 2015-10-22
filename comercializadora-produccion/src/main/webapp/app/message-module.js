'use strict';

var messageModule = angular.module('tikal.modules.Message', ['ngRoute','ngResource']);

messageModule.factory('tikal.modules.Error.Interceptor', function ($rootScope, $q, $window, $log, AUTH_EVENTS) {
  return {
    responseError: function (response) {
		//$log.info(response);
		if (response.status == -1) {
			$rootScope.$broadcast('ocurrio-error', 'Error de comunicación con el servidor');
		} else {
			angular.forEach(response.data.code, function(value, key) {
				if (response.data.message[key].length > 0) {
					//$log.info('hay mensaje');
					$rootScope.$broadcast('ocurrio-error', response.data.message[key]);
				} else {
					//$log.info('se imprime el codigo' + value[0]);
					$rootScope.$broadcast('ocurrio-error', value[0]);
				}
			});
		}
		return $q.reject(response);
    }
  };
});

messageModule.config(function ($routeProvider, $httpProvider) {
	$httpProvider.interceptors.push('tikal.modules.Error.Interceptor');
});


messageModule.controller('tikal.modules.Message.ReportCtrl', ['$scope', '$rootScope', '$location', '$log',
  function ($scope, $rootScope, $location, $log) {
	var enabled = true;
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
	$scope.$on('$routeChangeStart', function(event, data) {
		$scope.alerts = [];
	});
	$rootScope.$on('modal.show', function(event, data) {
		$scope.alerts = [];
		enabled = false;
	});
	$rootScope.$on('modal.hide', function(event, data) {
		enabled = true;
	});
	$scope.$on('ocurrio-error', function(event, data) {
		if (enabled) {
			$scope.alerts.push({type: 'danger', msg: data});
		}
	});
	$scope.$on('inicia-submit', function(event, data) {
		$scope.alerts = [];
	});
	$scope.$on('operacion-exitosa', function(event, data) {
		if (enabled) {
			$scope.alerts.push({type: 'success', msg: data});
		}
	});
}]);
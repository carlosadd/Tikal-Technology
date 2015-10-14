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

var messageModule = angular.module('tikal.modules.Message', ['ngRoute','ngResource']);

messageModule.factory('tikal.modules.Error.Interceptor', function ($rootScope, $q, $window, $log, AUTH_EVENTS) {
  return {
    responseError: function (response) {
		//$log.info('gato');
		if (response.status == 0) {
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
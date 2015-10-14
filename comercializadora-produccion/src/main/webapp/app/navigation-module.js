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

var navigationModule = angular.module('tikal.modules.tolumex.Navigation', [
  'tikal.modules.Message',
  'tikal.modules.Account',
  'tikal.modules.Pedido',
  'tikal.modules.Fabricacion',
  'tikal.modules.Contacto',
  'tikal.modules.Catalogo',
  'menu.controllers',
  'ngRoute',
  'ui.bootstrap'
  ]);
  
// Clear browser cache (in development mode)
//
// http://stackoverflow.com/questions/14718826/angularjs-disable-partial-caching-on-dev-machine
/*navigationModule.run(function ($rootScope, $templateCache) {
  $rootScope.$on('$viewContentLoaded', function () {
    $templateCache.removeAll();
  });
});*/

navigationModule.config(function ($routeProvider, $httpProvider) {
  //$routeProvider.otherwise({redirectTo: '/pedido'});
});

navigationModule.directive('applicationMenu', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/menu.html'
  };
});

navigationModule.service('tikal.modules.Util.Json', ['$log',
	function ($log) {
	this.clean = function(jsonObject) {
		angular.forEach(jsonObject, function(value, key) {
			if (value === "" || value === null){
				delete jsonObject[key];
			}
		});
	};
}]);
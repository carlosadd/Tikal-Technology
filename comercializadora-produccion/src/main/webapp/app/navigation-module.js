'use strict';

var navigationModule = angular.module('tikal.modules.tolumex.Navigation', [
  'tikal.modules.Message',
  'tikal.modules.Account',
  'tikal.modules.Pedido',
  'tikal.modules.Fabricacion',
  'tikal.modules.Almacen',
  'tikal.modules.Envio',
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
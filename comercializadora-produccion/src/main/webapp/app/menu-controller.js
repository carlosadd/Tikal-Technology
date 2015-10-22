'use strict';

/* Controllers */

var app = angular.module('menu.controllers', []);

app.controller('MenuCtrl', ['$scope', '$location',
  function ($scope, $location) { 
	$scope.isCollapsed = true;
    $scope.isActiveOption = function (viewLocation) { 
		if ($location.path().indexOf(viewLocation) == 0) {
			return 'active';
		}
    };
  }]);
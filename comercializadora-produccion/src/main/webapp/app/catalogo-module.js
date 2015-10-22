'use strict';

var catalogoProductoModule = angular.module('tikal.modules.Catalogo', ['ngRoute','ngResource','angular-ladda']);

catalogoProductoModule.config(function ($routeProvider, $httpProvider) {
	$routeProvider.when('/catalogo', {templateUrl: 'view/catalogo/catalogo-list.html', controller: 'tikal.modules.Catalogo.ListCtrl'});
	$routeProvider.when('/catalogo/:catalogoId', {templateUrl: 'view/catalogo/catalogo-detail.html', controller: 'tikal.modules.Catalogo.CrudCtrl'});
	$routeProvider.when('/catalogo/:catalogoId/producto/:productoId', {templateUrl: 'view/catalogo/producto/producto-edicion.html', controller: 'tikal.modules.Catalogo.producto.EditCtrl'});
	$routeProvider.when('/catalogo/:catalogoId/lineaProducto/:lineaProductoId', {templateUrl: 'view/catalogo/producto/linea-producto-edicion.html', controller: 'tikal.modules.Catalogo.linea.EditCtrl'});
});

catalogoProductoModule.controller('tikal.modules.Catalogo.ListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	};
	$scope.navegacion.actual.display = 'Catalogos';
	
	var catalogoDao = $resource(config.address + '/api/catalogo/producto/:catalogoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadCatalogos = function(){
		catalogoDao.query().$promise.then(function(data) {
			$scope.catalogos = data;
			$scope.catalogos.items.sort(function(a,b){
				var dateA = new Date(a.fechaDeCreacion.split('+')[0]);
				var dateB = new Date(b.fechaDeCreacion.split('+')[0]);
				if (dateA < dateB) {
					return -1;
				}
				if (dateB < dateA) {
					return 1;
				}
				return 0;
			});
		}, function(errResponse) {

		});
	};
	$scope.loadCatalogos();
	$scope.verCatalogo = function(catalogoId) {
	  $location.path($location.path() + '/' + catalogoId);
	};
	$scope.borrarCatalogo = function(catalogoId, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar el catalogo?',
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
		/*modalInstance.result.then(function (modelo) {
		});*/
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			catalogoDao.delete({catalogoId:catalogoId}).$promise.then(function(data) {
				$scope.loadCatalogos();
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
	$scope.nuevoCatalogo = function(size) {
		$scope.modeloNuevoContacto = {
			type:"CatalogoOfy",
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				catalogoDao.save($modalScope.modelo, function (data, headers) {
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
			templateUrl: 'view/catalogo/catalogo-nuevo.html',
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

catalogoProductoModule.controller('tikal.modules.Catalogo.CrudCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$log', '$uibModal', '$window',
  function ($scope, $resource, $routeParams, config, $log, $uibModal, $window) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Catalogos',
		link: '/catalogo/'
	});
	$scope.catalogoDao = $resource(config.address + '/api/catalogo/producto/:catalogoId',{catalogoId:$routeParams.catalogoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.catalogoDao.get().$promise.then(function(data) {
		$scope.catalogo = data;
		$scope.navegacion.actual.display = $scope.catalogo.nombre;
		$window.sessionStorage.setItem('nombreCatalogoActual', $scope.catalogo.nombre);
	}, function(errResponse) {
	
	});
	$scope.editar = function(size) {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/catalogo/catalogo-edicion.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.catalogo;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			$scope.catalogo = modelo;
			$scope.navegacion.actual.display = $scope.catalogo.nombre;
			$window.sessionStorage.setItem('nombreCatalogoActual', $scope.catalogo.nombre);
		});
		$scope.catalogo.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.catalogoDao.save($modalScope.modelo).$promise.then(
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

catalogoProductoModule.controller('tikal.modules.Catalogo.producto.ListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.productos = [];
	var productosDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/productos/:idProducto',{catalogoId:$routeParams.catalogoId}, {
		'query':  {method:'GET', isArray:false}
	});
	var loadProductos = function() {
		productosDao.query().$promise.then(function(data) {
			$scope.productos = $scope.productos.concat(data.items);
			$scope.productos.sort(function(a,b){
				return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
			});
		}, function(errResponse) {

		});
	};
	loadProductos();
	var lineaDeProductos = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:idProducto',{catalogoId:$routeParams.catalogoId}, {
		'query':  {method:'GET', isArray:false}
	});
	var loadLineasProductos = function() {
		lineaDeProductos.query().$promise.then(function(data) {
			$scope.productos = $scope.productos.concat(data.items);
			$scope.productos.sort(function(a,b){
				return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
			});
		}, function(errResponse) {

		});
	};
	loadLineasProductos();
	$scope.verProducto = function(id, type) {
		if (type =='LineaProductosPorTalla') {
			$location.path($location.path() + '/lineaProducto/' + id);
		}
		if (type =='ProductoIntermediario') {
			$location.path($location.path() + '/producto/' + id);
		}
	};
	
	$scope.borrarProducto = function(idProducto, type, index, size) {
		var dialogModel = {
			message: '¿Esta seguro de querer borrar el producto?',
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
		if (type =='LineaProductosPorTalla') {
			dialogModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				lineaDeProductos.delete({idProducto:idProducto}).$promise.then(function(data) {
					$scope.productos.splice(index, 1);
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, function(errResponse) {
					$modalScope.actionLoading = false;
				});
			};
		}
		if (type =='ProductoIntermediario') {
			dialogModel.command = function($modalScope) {
				$modalScope.actionLoading = true;
				productosDao.delete({idProducto:idProducto}).$promise.then(function(data) {
					$scope.productos.splice(index, 1);
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, function(errResponse) {
					$modalScope.actionLoading = false;
				});
			};
		}
	};
	
	$scope.nuevoProducto = function(size) {
		$scope.modeloNuevoContacto = {
			type:"ProductoIntermediario",
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				productosDao.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					$location.path($location.path() + '/producto/' + newId);
				}).$promise.then(
					function(data) {}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/catalogo/producto/producto-nuevo.html',
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
	$scope.nuevaLineaDeProducto = function(size) {
		$scope.modeloNuevoContacto = {
			type:"LineaProductosPorTalla",
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				lineaDeProductos.save($modalScope.modelo, function (data, headers) {
					var newId = headers('Location').split('/').pop();
					$location.path($location.path() + '/lineaProducto/' + newId);
				}).$promise.then(
					function(data) {}, 
					function(errResponse) {
						$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/catalogo/producto/producto-nuevo.html',
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

catalogoProductoModule.controller('tikal.modules.Catalogo.producto.EditCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$log', '$uibModal', '$window',
  function ($scope, $resource, $routeParams, config, $location, $log, $uibModal, $window) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Catalogos',
		link: '/catalogo/'
	});
	$scope.navegacion.previos.push({
		display: $window.sessionStorage.getItem('nombreCatalogoActual'),
		link: '/catalogo/' + $routeParams.catalogoId
	});
	$scope.productoDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/productos/:productoId',{catalogoId:$routeParams.catalogoId, productoId:$routeParams.productoId}, {
		'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.productoDao.get().$promise.then(function(data) {
		$scope.modelo = data;
		$scope.navegacion.actual.display = $scope.modelo.datosGenerales.nombre;
	}, function(errResponse) {
	
	});
	$scope.ok = function(form) {
		$scope.actionLoading = true;
		delete $scope.modelo['catalogoId'];
		$scope.productoDao.save($scope.modelo).$promise.then(
			function(data) {
				form.$setPristine();
				form.$setUntouched();
				$scope.actionLoading = false;
				$scope.navegacion.actual.display = $scope.modelo.datosGenerales.nombre
			},
			function(errResponse) {
				$scope.actionLoading = false;
		});	
	};
	$scope.close = function() {
		$location.path('/catalogo/' + $routeParams.catalogoId);
	}
}]);

catalogoProductoModule.controller('tikal.modules.Catalogo.linea.EditCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$log', '$uibModal', '$window',
  function ($scope, $resource, $routeParams, config, $location, $log, $uibModal, $window) {
	//navegacion
	$scope.navegacion = {
		actual:{
			display:''
		},
		previos:[]
	}
	$scope.navegacion.previos.push({
		display: 'Catalogos',
		link: '/catalogo/'
	});
	$scope.navegacion.previos.push({
		display: $window.sessionStorage.getItem('nombreCatalogoActual'),
		link: '/catalogo/' + $routeParams.catalogoId
	});
	$scope.lineaProductoId = $routeParams.lineaProductoId;
	$scope.lineaDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:lineaProductoId',
		{catalogoId:$routeParams.catalogoId, lineaProductoId:$routeParams.lineaProductoId}, 
		{'get':  {method:'GET', isArray:false, params:{}}
	});
	$scope.lineaDao.get().$promise.then(function(data) {
		$scope.modelo = data;
		$scope.navegacion.actual.display = $scope.modelo.datosGenerales.nombre;
	}, function(errResponse) {
	
	});
	$scope.ok = function(form) {
		$scope.actionLoading = true;
		delete $scope.modelo['catalogoId'];
		$scope.lineaDao.save($scope.modelo).$promise.then(
			function(data) {
				form.$setPristine();
				form.$setUntouched();
				$scope.actionLoading = false;
				$scope.navegacion.actual.display = $scope.modelo.datosGenerales.nombre
			},
			function(errResponse) {
				$scope.actionLoading = false;
		});	
	};
	$scope.close = function() {
		$location.path('/catalogo/' + $routeParams.catalogoId);
	}
}]);

catalogoProductoModule.controller('tikal.modules.Catalogo.linea.producto.ListCtrl', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.productosLineaDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:lineaProductoId/productos/:productoId',
		{catalogoId:$routeParams.catalogoId, lineaProductoId:$scope.lineaProductoId, productoId:'@id'}, 
		{'query':  {method:'GET', isArray:false}
	});
	$scope.loadProductos = function(){
		$scope.productosLineaDao.query().$promise.then(function(data) {
			$scope.productos = data.items;
			$scope.productos.sort(function(a,b){
				return a.talla.localeCompare(b.talla);
			});
		}, function(errResponse) {

		});
	};
	$scope.loadProductos();
	$scope.nuevaTalla = function(size) {
		$scope.modeloNuevaTalla = {
			type:"ProductoConTalla",
			command: function($modalScope) {
				$modalScope.actionLoading = true;
				$scope.productosLineaDao.save($modalScope.modelo).$promise.then(function(data) {
					$scope.loadProductos();
					$modalScope.actionLoading = false;
					modalInstance.close($modalScope.modelo);
				}, function(errResponse) {
					$modalScope.actionLoading = false;
				});	
			}
		};
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/catalogo/producto/linea-producto-talla.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return $scope.modeloNuevaTalla;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//nada
		});
	};
	$scope.editarTalla = function(index, size) {
		var original = $scope.productos[index];
		//delete original['lineaDeProductos'];
		original.command = function($modalScope) {
			$modalScope.actionLoading = true;
			delete $modalScope.modelo['lineaDeProductosId'];
			delete $modalScope.modelo['catalogoId'];
			$scope.productosLineaDao.save($modalScope.modelo).$promise.then(function(data) {
				$scope.loadProductos();
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		}
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'view/catalogo/producto/linea-producto-talla.html',
			controller: 'tikal.modules.Contacto.ModalGenericEditCtrl',
			backdrop: 'static',
			size: size,
			resolve: {
				modelo: function () {
					return original;
				}
			}
		});
		modalInstance.result.then(function (modelo) {
			//$scope.productos[index] = modelo;
		});
	};
	$scope.borrarTalla = function(index, producto, size) {
		var original = $scope.productos[index];
		var dialogModel = {
			message: '¿Esta seguro de querer borrar el producto?',
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
		dialogModel.command = function($modalScope) {
			$modalScope.actionLoading = true;
			$scope.productosLineaDao.delete({productoId:original.id}).$promise.then(function(data) {
				$scope.productos.splice(index, 1);
				$modalScope.actionLoading = false;
				modalInstance.close($modalScope.modelo);
			}, function(errResponse) {
				$modalScope.actionLoading = false;
			});
		};
	};
}]);

catalogoProductoModule.controller('tikal.modules.Catalogo.producto.seleccion', ['$scope', '$resource', '$routeParams', 'PRODUCCION_REMOTE_ADDRESS', '$location', '$uibModal', '$log',
  function ($scope, $resource, $routeParams, config, $location, $uibModal, $log) {
	$scope.catalogoDao = $resource(config.address + '/api/catalogo/producto/:catalogoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.productoDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/productos/:productoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.lineaDeProductosDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:lineaProductoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.productosLineaDao = $resource(config.address + '/api/catalogo/producto/:catalogoId/lineaProducto/:lineaProductoId/productos/:productoId',{}, {
		'query':  {method:'GET', isArray:false}
	});
	$scope.loadCatalogos = function() {
		$scope.catalogoDao.query().$promise.then(function(data) {
			$scope.catalogos = data.items;
			$scope.catalogos.sort(function(a,b){
				var dateA = new Date(a.fechaDeCreacion.split('+')[0]);
				var dateB = new Date(b.fechaDeCreacion.split('+')[0]);
				if (dateA < dateB) {
					return -1;
				}
				if (dateB < dateA) {
					return 1;
				}
				return 0;
			});
		}, function(errResponse) {

		});
	};
	$scope.loadProductos = function(catalogoId) {
		$scope.productoDao.query({catalogoId:catalogoId}).$promise.then(function(data) {
			data.items.sort(function(a,b){
				return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
			});
			angular.forEach(data.items, function(dato, key) {
				dato.display = $scope.getProductoDisplay(dato);
			});
			$scope.productos = $scope.productos.concat(data.items);
		}, function(errResponse) {

		});
	};
	$scope.loadLineasProductos = function(catalogoId) {
		$scope.lineaDeProductosDao.query({catalogoId:catalogoId}).$promise.then(function(data) {
			angular.forEach(data.items, function(dato, key) {
				dato.display = $scope.getProductoDisplay(dato);
			});
			$scope.productos = $scope.productos.concat(data.items);
			$scope.productos.sort(function(a,b){
				return a.datosGenerales.nombre.localeCompare(b.datosGenerales.nombre);
			});
		}, function(errResponse) {

		});
	};
	$scope.loadProductosLinea = function(lineaProductoId) {
		$scope.productosLineaDao.query({catalogoId:$scope.catalogoSeleccionado.id, lineaProductoId:lineaProductoId}).$promise.then(function(data) {
			$scope.subProductos = data.items;
			$scope.subProductos.sort(function(a,b){
				return a.talla.localeCompare(b.talla);
			});
			}, function(errResponse) {
		});
	}
	$scope.$watch('catalogoSeleccionado', function(newValue, oldValue) {
		$scope.productoSeleccionado = null;
		$scope.subProductoSeleccionado = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id) {
					$scope.productos = [];
					$scope.loadProductos(newValue.id);
					$scope.loadLineasProductos(newValue.id);
				}
			} else {
				$scope.productos = [];
				$scope.loadProductos(newValue.id);
				$scope.loadLineasProductos(newValue.id);
			}
		}
	});
	$scope.$watch('productoSeleccionado', function(newValue, oldValue) {
		$scope.subProductoSeleccionado = null;
		if (newValue && newValue.id) {
			if (oldValue && oldValue.id) {
				if (newValue.id != oldValue.id && $scope.productoSeleccionado.type == 'LineaProductosPorTalla') {
					$scope.loadProductosLinea(newValue.id);
				}
			} else {
				if ($scope.productoSeleccionado.type == 'LineaProductosPorTalla') {
					$scope.loadProductosLinea(newValue.id);
				}
			}
		}
	});
	$scope.loadCatalogos();
	$scope.getProductoDisplay = function(producto) {
		if (producto && producto.datosGenerales) {
			var respuesta = producto.datosGenerales.nombre;
			if (producto.datosGenerales.descripcion) {
				respuesta = respuesta + " " + producto.datosGenerales.descripcion;
			}
			return respuesta;
		}
		return null;
	};
	$scope.seleccionarProducto = function() {
		if ($scope.productoSeleccionado && $scope.productoSeleccionado.type) {
			if ($scope.productoSeleccionado.type == 'ProductoIntermediario') {
				var producto = angular.copy($scope.productoSeleccionado);
				delete producto['display'];
				$scope.$emit('seleccionProducto', producto);
			} else {
				var producto = angular.copy($scope.productoSeleccionado);
				var subProducto = angular.copy($scope.subProductoSeleccionado);
				subProducto.datosGenerales = $scope.productoSeleccionado.datosGenerales;
				producto.subProducto = subProducto;
				$scope.$emit('seleccionSubProducto', producto);
			}
		}
	};
	$scope.$on('limpiarSeleccionSubProducto', function (event, data) {
		$scope.subProductoSeleccionado = null;
	});
}]);

catalogoProductoModule.directive('productosCatalogo', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/catalogo/producto/producto-list.html'
  };
});
catalogoProductoModule.directive('lineaProductosTallas', function() {
  return {
    restrict: 'E',
    templateUrl: 'view/catalogo/producto/linea-producto-talla-list.html'
  };
});
catalogoProductoModule.directive('seleccionProducto', function() {
  return {
    restrict: 'E',
	controller:'tikal.modules.Catalogo.producto.seleccion', 
    templateUrl: 'view/catalogo/producto/producto-seleccion.html'
  };
});
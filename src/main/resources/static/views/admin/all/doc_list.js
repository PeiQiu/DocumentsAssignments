var allDocs = angular.module('Docstar.Admin.DocList', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session','ui.router']);

allDocs.config(function($stateProvider,$routeProvider){
    $routeProvider.when('/admin/lists', {
        templateUrl: 'views/admin/all/doc_list.html',
        controller: 'Docstar.Admin.DocList.Controller'});

});
allDocs.controller('Docstar.Admin.DocList.Controller', ['$scope','$resource','$http','$location','Session.Service','$state','$stateParams',function( $scope,$resource,$http,$location,Session,$state,$stateParams) {
   getDocList();
    $scope.edit=function (doclist) {
        $state.go('docs', {doclist:doclist});
    };
    function getDocList() {
        $http({
            method: 'GET',
            url: '/api/v1/admin/doclist',
        }).then(function successCallback(response) {
            $scope.doclists = response.data;

        }, function errorCallback(response) {
            Session.destroy();
            $location.path('/login');
        });
    }

}]);



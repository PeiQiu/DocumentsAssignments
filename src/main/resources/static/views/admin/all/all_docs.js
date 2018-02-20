var allDocs = angular.module('Docstar.Admin.Docs', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session','ui.router']);

allDocs.config(function($stateProvider,$routeProvider){
    $routeProvider.when('/admin/docs', {
        templateUrl: 'views/admin/all/all_docs.html',
        controller: 'Docstar.Admin.Docs.Controller'
    })

    $stateProvider.state('docs', {
        url: '/admin/docs',
        cache:'false',
        params: {doclist: null},
        templateUrl: 'views/admin/all/all_docs.html',
        controller: 'Docstar.Admin.Docs.Controller'
    })

});
allDocs.controller('Docstar.Admin.Docs.Controller', ['$scope','$resource','$http','$location','Session.Service','$state','$stateParams',function( $scope,$resource,$http,$location,Session,$state,$stateParams) {
    $scope.mode=0;
    $scope.page=1;
    $scope.isDoc=true;
    $scope.filter={};
    $scope.doclist=[];
    $scope.userNameList=[];
    $scope.docMap=[];
    var filter=getQuery();
    getDocs(filter);
    getUsers({});

    if($stateParams.doclist){
    $scope.doclist=$stateParams.doclist.docs;
    $scope.userNameList=$stateParams.doclist.users;
    $scope.doclistId=$stateParams.doclist.id;
    for(var i=0;i< $scope.doclist.length;i++){
        $scope.docMap.push($scope.doclist[i].document_number);
    }
        $stateParams.doclist=null;
    }
    $scope.next=function () {
      $scope.page++;
      filter.page++;
        getDocs(filter);

    };
    $scope.prev=function () {
        $scope.page--;
        filter.page--;
        getDocs(filter);
    };
    $scope.edit=function () {
        $scope.mode= $scope.mode?0:1;
    };
    $scope.search=function(field){
        filter=getQuery();
        $scope.page=1;
        filter.page=1;
        getDocs(filter);
    };

    $scope.add=function(doc,num){
        if($scope.docMap.indexOf(num)==-1){
            $scope.docMap.push(num);
            $scope.doclist.push(doc);
        }

    };
    $scope.removeDoc=function(index){
        $scope.doclist.splice(index,1);
        $scope.docMap.splice(index,1);
    };
    $scope.showDocs=function () {
       $scope.isDoc=true;
    };
    $scope.showUsers=function () {
        $scope.isDoc=false;
    };
    $scope.save=function () {
        if($scope.doclist.length<=0){
            alert('Empty doc list');
            return;
        }
        if($scope.userNameList.length<=0){
            alert('Empty user list');
            return;
        }
        if($scope.doclistId){
            $http({
                method:'PUT',
                url:'/api/v1/admin/doclist/'+$scope.doclistId,
                data:{docs:$scope.doclist,
                    users:$scope.userNameList},
                headers:{'Content-Type': 'application/json'},
            }).then(function successCallback(req) {
                if(req){
                    $scope.doclist=[];
                    $scope.userNameList=[];
                }

            }, function errorCallback(response) {

            })
        }else {
        $http({
            method:'POST',
            url:'/api/v1/admin/doclist',
            data:{docs:$scope.doclist,
                  users:$scope.userNameList},
            headers:{'Content-Type': 'application/json'},
        }).then(function successCallback(req) {
            if(req){
                $scope.doclist=[];
                $scope.userNameList=[];
            }

        }, function errorCallback(response) {

        })
        }
    };
    function getQuery() {
        var query={};
        query.page=$scope.page;
        query.title=$scope.filter.title;
        query.type=$scope.filter.type;
        query.zipcode=$scope.filter.zipcode;
        query.significant=$scope.filter.significance;
        return query;
    }
    function getDocs(params) {
        $http({
            method: 'GET',
            url: '/api/v1/admin/docs',
            params:params,
        }).then(function successCallback(response) {
            $scope.docs = response.data.results;
            $scope.data=response.data;

        }, function errorCallback(response) {
            Session.destroy();
            $location.path('/login');
        });
    }
    function getUsers(params) {
        var Users = $resource('api/v1/admin/users?role=ROLE_USER', {});
        $scope.users = Users.query();
    }

    $scope.addUser=function(name){
        if($scope.userNameList.indexOf(name)==-1){
            $scope.userNameList.push(name);
        }

    };
    $scope.removeUser=function(index){
        $scope.userNameList.splice(index,1);
    };

}]);
allDocs.filter('ecplise',function(){
    return function(input){
        if(!input){
            input="";
        }
        if(input.length>256){
            input= input.substring(0,256);
        }
        return input;
    }
})
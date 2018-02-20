users = angular.module('Docstar.Users', ['ngRoute', 'ngResource', 'Session']);

users.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/admin/users', {
        templateUrl: 'views/admin/users/users.html',
        controller: 'Docstar.Users.Controller'
    });
}]);

users.controller('Docstar.Users.Controller', ['$scope', '$resource', 'Session.Service', '$http','$location', function ($scope, $resource, Session, $http,$location) {
    var Users = $resource('api/v1/admin/users/:uid', {});
    $scope.users = Users.query();
    $scope.user = Session.user();
    $scope.userUpdate = {};
    $scope.userCreate={

        authorities:[]
    };
    $scope.mode = 1;
    $scope.hasRole = function (user, role) {
        for (var i = 0; i < user.authorities.length; i++) {
            if (user.authorities[i].authority === role) return true;
        }
        return false;
    }
    $scope.updateRole = function (user, role) {
        if (user.id == $scope.user.id && role == 'ROLE_ADMIN') {
            alert('Can not Remove the ADMIN role');
            return;
        }
        $http({
            method: 'PUT',
            url: '/api/v1/admin/users/role/' + user.id,
            data: role,
            headers: {'Content-Type': 'application/json'},

        }).then(function successCallback(req) {
            if (req && req.id == $scope.user.id) {

                Session.create(req);
                $scope.user = Session.user();
            }
        }, function errorCallback(response) {

        });
    }

    $scope.updateEnable= function (user) {
        if (user.id == $scope.user.id && role == 'ROLE_ADMIN') {
            alert('Can not disable your account');
            return;
        }
        $http({
            method: 'PUT',
            url: '/api/v1/admin/users/enable/' + user.id,
            headers: {'Content-Type': 'application/json'},

        }).then(function successCallback(req) {

        }, function errorCallback(response) {

        });
    }
    $scope.changePassword = function () {
        if ($scope.userUpdate.password && $scope.userUpdate.password2) {
            if ($scope.userUpdate.password2 != $scope.userUpdate.password) {
                alert('Password mismatch.');
                return;
            }
            $http({
                method: 'PUT',
                url: '/api/v1/admin/users/profile/' + $scope.currentUser.id,
                data: $scope.userUpdate,
                headers: {
                    'Content-Type': 'application/json'
                },

            }).then(function successCallback(req) {
                $scope.userUpdate = null;
                $scope.mode=1;
                }, function errorCallback(response) {

                });
        }
    }
    $scope.close=function () {
        $scope.mode=1;
    }
    $scope.openChangePassword = function (user) {
        $scope.currentUser = user;
        $scope.mode=2;
    }
    $scope.openCreateUser=function () {
        $scope.mode=3;
    }
    $scope.createUser=function(){
        if($scope.userCreate.username&&$scope.userCreate.password&&$scope.userCreate.password2&&$scope.userCreate.email
            &&$scope.userCreate.password==$scope.userCreate.password2){
            delete $scope.userCreate.password2;
            console.log($scope.userCreate );
            for(var i=0;i<$scope.users.length;i++){
                if($scope.users[i].username==$scope.userCreate.username){
                    alert('Username already exists');
                    return ;
                }
            }
            $http({
                method: 'POST',
                url: '/api/v1/admin/users',
                data: $scope.userCreate,
                headers: {
                    'Content-Type': 'application/json'
                },
            }).success(function (req) {
                $scope.userCreate = null;
                $scope.users.push(req);
                $scope.mode=1;
            })


        }else {
            alert('Invalid Input');
        }
    }
    $scope.role=function(role){
        var index=-1;
    for(var i=0;i<$scope.userCreate.authorities.length;i++){
        if($scope.userCreate.authorities[i].authority==role){
            index=i;
            break;
        }
    }
    if(index==-1){
        $scope.userCreate.authorities.push({authority:role});
    }else {
        $scope.userCreate.authorities.splice(index,1);
    }

    }
}]);

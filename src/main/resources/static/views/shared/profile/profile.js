var profile = angular.module('Docstar.Profile', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session']);

profile.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/profile', {
        templateUrl: 'views/shared/profile/profile.html',
        controller: 'Docstar.Profile.Controller'
    });

}]);

profile.controller('Docstar.Profile.Controller', ['$scope', '$resource', '$uibModal', 'Session.Service', 'fileReader', '$http', '$location', function ($scope, $resource, $modal, Session, fileReader, $http, $location) {
    $scope.user = Session.user();
    $scope.updateProfile = function () {
        if ($scope.userUpdate.password || $scope.userUpdate.password2) {
            if (!($scope.userUpdate.password && $scope.userUpdate.password2 && $scope.userUpdate.password2 == $scope.userUpdate.password)) {
                alert('Password mismatch.');
                return;
            }
        }
        $http({
            method: 'PUT',
            url: '/api/v1/profile/' + $scope.user.id,
            data: $scope.userUpdate,
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(function successCallback(req) {
            Session.create(req);
            $scope.user = Session.user();
            console.log(req);
        }, function errorCallback(response) {
            
        });
    }
    $scope.userUpdate = {phones: $scope.user.phones || []};
    $scope.addPhone = function () {
        if ($scope.phone && $scope.userUpdate.phones.indexOf($scope.phone) == -1) {
            $scope.userUpdate.phones.push($scope.phone);
        }
        $scope.phone = "";
    };
    $scope.deletePhone = function (index) {
        $scope.userUpdate.phones.splice(index, 1);
    };
    $scope.img_src = "/api/v1/profile/avatars/" + Session.user().id;

    $scope.img_srcd = "api/v1/dels";

    $scope.loadImage = function (img) {
        console.log(img[0]);
        fileReader.readAsDataUrl(img[0], $scope)
            .then(function (result) {
                console.log("read image file result", result);
                $scope.img_src = result;
                //uploader.uploadAll();
                $http({
                    method: 'POST',
                    url: '/api/v1/profile/avatars/' + $scope.user.id,
                    headers: {'Content-Type': undefined},
                    data: {
                        file: img[0],
                    },
                    transformRequest: (data) => {
                        let formData = new FormData();
                        angular.forEach(data, function (value, key) {
                            formData.append(key, value);
                        });
                        return formData;
                    }
                }).success(() => {
                    alert('Upload Successfully');

                }).error(() => {
                    alert('Fail to upload, please upload again');
                });

            });
    };
}]);

profile.factory('fileReader', ["$q", function ($q) {
    var onLoad = function (reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.resolve(reader.result);
            });
        };
    };

    var onError = function (reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.reject(reader.result);
            });
        };
    };

    var getReader = function (deferred, scope) {
        var reader = new FileReader();
        reader.onload = onLoad(reader, deferred, scope);
        reader.onerror = onError(reader, deferred, scope);
        return reader;
    };

    var readAsDataURL = function (file, scope) {
        var deferred = $q.defer();
        var reader = getReader(deferred, scope);
        reader.readAsDataURL(file);
        return deferred.promise;
    };

    return {
        readAsDataUrl: readAsDataURL
    };

}])
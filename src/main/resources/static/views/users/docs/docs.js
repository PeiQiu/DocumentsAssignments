var docs = angular.module('Docstar.User.Docs', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session']);

docs.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/user/docs', {
        templateUrl: 'views/users/docs/docs.html',
        controller: 'Docstar.User.Docs.Controller'
    })

}]);

docs.controller('Docstar.User.Docs.Controller', ['$scope', '$resource', '$http', '$location', 'Session.Service', function ($scope, $resource, $http, $location, Session) {
    $scope.user = Session.user();
    $scope.filter = {};
    //rating bar
    $scope.max = 5;
    var filter = $scope.filter;
    getReviews(filter);
    $scope.search = function (field) {
        getReviews(filter);
    };
    $scope.rate = function (review) {
        $http({
            method: 'PUT',
            url: '/api/v1/user/' + $scope.user.id + '/docs/' + review.doc.document_number + '/rank',
            data: review.rank,
            headers: {'Content-Type': 'application/json'},

        }).success(function (req) {
        })
    }
    function getQuery() {
        var query = {};
        query.title = $scope.filter.title;
        query.type = $scope.filter.type;
        query.zipcode = $scope.filter.zipcode;
        query.significant = $scope.filter.significance;
        query.isReviewed = $scope.filter.isReviewed;
        return query;
    }

    function getReviews(params) {
        $http({
            method: 'GET',
            url: '/api/v1/user/docs/' + $scope.user.username,
            params: params,
        }).then(function successCallback(response) {
            $scope.reviews = response.data;
            for (var i = 0; i < $scope.reviews.length; i++) {
                $scope.reviews[i].isReviewed = ($scope.reviews[i].rank != -1);
            }
        }, function errorCallback(response) {
            Session.destroy();
            $location.path('/login');
        });
    }

}]);
docs.directive('starRating', function () {
    return {
        restrict: 'A',
        template: '<ul class="rating">' +
        '<li ng-repeat="star in stars" ng-class="star">' +
        '\u2605' +
        '</li>' +
        '</ul>',
        scope: {
            ratingValue: '=',
            max: '='
        },
        link: function (scope, elem, attrs) {
            scope.stars = [];
            for (var i = 0; i < scope.max; i++) {
                scope.stars.push({
                    filled: i < scope.rating
                });
            }
        }
    }
});

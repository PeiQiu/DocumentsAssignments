var app = angular.module('Docstar.App', [ 'ngRoute',
                                             'spring-security-csrf-token-interceptor', 
                                             'Session', 
                                             'Docstar.Login', 
                                             'Docstar.Profile',
                                             'Docstar.Users',
                                             'Docstar.User.Docs',
                                             'Docstar.Admin.Docs',
                                             'Docstar.Admin.DocList'



]);

app.controller('Docstar.Navigation.Controller', [ '$scope', '$http', '$location', 'Session.Service', function($scope, $http, $location, Session) {
	$scope.isAuthenticated = Session.isAuthenticated;
	
	$scope.username = function() {
		var user = Session.user();
		return user && user.username;
	};
    $scope.hasRole = Session.hasRole;
    $scope.logout = function() {
		var req = {
				method : 'POST',
				url : '/logout',
					};
			
		$http(req).then(
            function successCallback(response) {
                	Session.destroy();
                	$location.path('/login');

            }, function errorCallback(response) {
                Session.destroy();
                $location.path('/login');
            }

		);
	};

} ] );

app.config([ '$routeProvider', '$httpProvider',
    function($routeProvider, $httpProvider) {
        $httpProvider.interceptors.push(AuthInterceptor);
        $routeProvider.otherwise({
            redirectTo : '/login'
        });
    } ]);
function AuthInterceptor( $q , $location  ) {
    return {
        'responseError' : function( rejection  ) {
            //SessionService.destroy();

            if(rejection.status == 401 ){
                $location.path('/login');
            }
            return $q.reject( rejection );
        },
        'response' : function( rejection  ) {
            // alert("reserr")
            //SessionService.destroy();
            //alert(rejection.status)
            if(rejection.status == 401 ){
                $location.path('/login');
            }

            return rejection;
        }
    }
}
app.factory('AuthInterceptor', ['$q', '$location',  AuthInterceptor ] );




angular.module('Session', []).factory('Session.Service', function () {
   var service = {};

   service.user = function( ) {
      if( sessionStorage.user ) {
         return JSON.parse( sessionStorage.user );
      } else {
         return null;
      }
   }
   
   service.hasRole = function( role ) {
	   var user = service.user();
	   if( user ) {
		   for( var i = 0; i < user.authorities.length; i++) {
			   if( user.authorities[i].authority === role ) return true;
		   }
	   }
	   return false;
   }

   service.isAuthenticated = function() {
       return sessionStorage.user && true;
   }
   
   service.create  = function ( user ) {
      sessionStorage.user = JSON.stringify( user );
   };
   
   service.destroy = function () {
      delete sessionStorage.user;
   };

   return service;
} );


angular.module('sociogram', ['ionic', 'openfb', 'sociogram.controllers'])

.run(function($rootScope, $state, $ionicPlatform, $window, OpenFB) {

    //OpenFB.init('489987941127071','http://localhost:8080/BoaTroca-Ionic/www/oauthcallback.html',window.localStorage);
    //OpenFB.init('489987941127071','https://www.facebook.com/connect/login_success.html', window.localStorage);
    OpenFB.init('489987941127071');

    navigator.geolocation.getCurrentPosition(function(postion) {
        window.localStorage['latitude'] = postion.coords.latitude;
        window.localStorage['longitude'] = postion.coords.longitude;
    });

    $ionicPlatform.ready(function() {
        if (window.StatusBar) {
            StatusBar.styleDefault();
        }
    });

    $rootScope.$on('$stateChangeStart', function(event, toState) {
        if (toState.name !== "app.login" && toState.name !== "app.logout" && !$window.sessionStorage['fbtoken']) {
            $state.go('app.login');
            event.preventDefault();
        }
    });

    $rootScope.$on('OAuthException', function() {
        $state.go('app.login');
    });

})

.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider

    .state('app', {
        url: "/app",
        abstract: true,
        templateUrl: "templates/menu.html",
        controller: "AppCtrl"
    })

    .state('app.menuRight', {
        url: "/menu",
        views: {
            'menuContent': {
                templateUrl: "templates/menu.html",
                controller: "menuCtrl"
            }
        }
    })

    /*.state('app.trades', {
        url: "/app",
        views:{
            'menuContent':{
                templateUrl: "templates/trades.html",
                controller: "tradesCtrl"
            }
        }
    })*/

    .state('app.home', {
        url: "/home",
        views: {
            'menuContent': {
                templateUrl: "templates/home.html",
                controller: "HomeCtrl"
            }
        }
    })

    .state('app.login', {
        url: "/login",
        views: {
            'menuContent': {
                templateUrl: "templates/login.html",
                controller: "LoginCtrl"
            }
        }
    })

    .state('app.logout', {
        url: "/logout",
        views: {
            'menuContent': {
                templateUrl: "templates/logout.html",
                controller: "LogoutCtrl"
            }
        }
    })

    .state('app.mypictures', {
        url: "/mypictures",
        views: {
            'menuContent': {
                templateUrl: "templates/mypictures.html",
                controller: "MyPicturesCtrl"
            }
        }
    })

    .state('app.showpicture', {
        url: "/showpicture/:pictureId",
        views: {
            'menuContent': {
                templateUrl: "templates/showpicture.html",
                controller: "ShowPictureCtrl"
            }
        }
    })

    .state('app.registerpicture', {
        url: "/registerpicture",
        views: {
            'menuContent': {
                templateUrl: "templates/registerpicture.html",
                controller: "RegisterPicturerCtrl"
            }
        }
    })

    .state('app.chat', {
        url: '/chat',
        views: {
            'menuContent': {
                templateUrl: "templates/chat.html",
                controller: 'ChatCtrl'
            }
        }
    })

    .state('app.searchresult', {
        url: "/searchresult",
        views: {
            'menuContent': {
                templateUrl: "templates/searchresult.html",
                controller: "searchResultCtrl"
            }
        }
    })

    .state('app.userpictures', {
        url: "/userpictures",
        views: {
            'menuContent': {
                templateUrl: "templates/userpictures.html",
                controller: "userPicturesCtrl"
            }
        }
    })

    .state('app.categories', {
        url: "/categories",
        views: {
            'menuContent': {
                templateUrl: "templates/categories.html",
                controller: "categoriesCtrl"
            }
        }
    });

    // fallback route
    $urlRouterProvider.otherwise('/app/home');

});

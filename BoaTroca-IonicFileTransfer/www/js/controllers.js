
angular.module('sociogram.controllers', [])

.controller('AppCtrl', function($scope, $state, OpenFB) {

    $scope.logout = function() {
        OpenFB.logout();
        $state.go('app.login');
    };

    $scope.revokePermissions = function() {
        OpenFB.revokePermissions().then(
            function() {
                $state.go('app.login');
            },
            function() {
                alert('Revoke permissions failed');
            });
    };
})

.controller('menuCtrl', function($scope, $rootScope, $http, $state, $window, OpenFB) {
    $scope.data = {};
    //$interval(
    $rootScope.$on('$stateChangeStart', function(event, toState) {
        if (toState.name !== "app.login" && toState.name !== "app.logout" && !$window.sessionStorage['fbtoken']) {
            $state.go('app.login');
            event.preventDefault();
        } else {
            var userJson = {
                'user.id': window.localStorage['sessao.userId']
            };

            $http({
                method: 'GET',
                url: 'http://192.168.1.5:8080/barterserver/trade/post/list',
                params: userJson
            })
                .success(function(data, status, headers, config) {
                    //alert(JSON.stringify(data[0]));
                    $scope.items = data;

                })
                .error(function(data) {
                    alert(data);
                });
        }

    });
    //}), 10);

    $scope.userPictures = function(tradeId, userReqId, userReqName) {
        $state.go('app.home');

        $rootScope.tradeId = null;
        $rootScope.userReqId = null;
        $rootScope.userReqName = null;
        $rootScope.tradeId = tradeId;
        $rootScope.userReqId = userReqId;
        $rootScope.userReqName = userReqName;

        if ($rootScope.tradeId) {

            $state.go('app.userpictures');
        } else {
            alert("Erro!");
        }
    };

    $scope.goChat = function(tradeId) {
        $rootScope.tradeId = tradeId;
        $state.go('app.chat');
    };
})

.controller('LoginCtrl', function($http, $scope, $location, OpenFB) {
    $scope.facebookLogin = function() {

        //REDIRECTT!!!! Retirar**************
        //$location.path('/app/home');
        
        OpenFB.login('email, user_birthday').then(
            function(success) {
                OpenFB.get('/me').success(function(user) {

                    var date = new Date();                    
                    var year = date.getFullYear();
                    var month = date.getMonth() + 1;
                    if (month < 10) {
                        month = "0" + month;
                    }
                    var day = date.getDate();

                    var values = user.birthday.split("/");
                    var dayOfBirth = values[1];
                    var monthOfBirth = values[0];
                    var yearOfBirth = values[2];

                    var age = year - yearOfBirth;

                    if (month < monthOfBirth) {
                        age = age - 1;
                    }

                    if (month == monthOfBirth && day < dayOfBirth) {
                        age = age - 1;
                    }

                    var latitude = window.localStorage['latitude'];
                    var longitude = window.localStorage['longitude'];
                    var password = hex_md5(user.id);
                    var accessToken = window.sessionStorage['fbtoken'];

                    /* TEMOS QUE FAZER O MD5 */                    

                    var userJson = {
                        'user.name': user.name,
                        'user.email': user.email,
                        'user.age': age,
                        'user.password': password,
                        'user.loc_lat': latitude,
                        'user.loc_long': longitude,
                        'user.accessToken': accessToken
                    };

                    $http({
                        method: 'GET',
                        url: 'http://192.168.1.5:8080/barterserver/user/post/save',
                        params: userJson
                    }).
                    success(function(data, status, headers, config) {
                        // this callback will be called asynchronously
                        // when the response is available
                        window.localStorage['sessao.userId'] = data.id;
                        window.localStorage['sessao.name'] = data.name;
                        window.localStorage['sessao.email'] = data.email;
                        window.localStorage['sessao.age'] = data.age;
                        window.localStorage['sessao.password'] = data.password;
                        window.localStorage['sessao.accessToken'] = data.accessToken;


                        //REDIRECTT!!!!
                        $location.path('/app/home');
                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        alert("Erro!");
                    });
                });
            },
            function() {
                alert('OpenFB login failed');
            });
    };

    var hexcase = 0;

    function hex_md5(a) {
        return rstr2hex(rstr_md5(str2rstr_utf8(a)));
    }

    function hex_hmac_md5(a, b) {
        return rstr2hex(rstr_hmac_md5(str2rstr_utf8(a), str2rstr_utf8(b)));
    }

    function md5_vm_test() {
        return hex_md5("abc").toLowerCase() == "900150983cd24fb0d6963f7d28e17f72";
    }

    function rstr_md5(a) {
        return binl2rstr(binl_md5(rstr2binl(a), a.length * 8));
    }

    function rstr_hmac_md5(c, f) {
        var e = rstr2binl(c);
        if (e.length > 16) {
            e = binl_md5(e, c.length * 8);
        }
        var a = Array(16),
            d = Array(16);
        for (var b = 0; b < 16; b++) {
            a[b] = e[b] ^ 909522486;
            d[b] = e[b] ^ 1549556828;
        }
        var g = binl_md5(a.concat(rstr2binl(f)), 512 + f.length * 8);

        return binl2rstr(binl_md5(d.concat(g), 512 + 128));
    }

    function rstr2hex(c) {
        try {
            hexcase;
        } catch (g) {
            hexcase = 0;
        }
        var f = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
        var b = "";
        var a;
        for (var d = 0; d < c.length; d++) {
            a = c.charCodeAt(d);
            b += f.charAt((a >>> 4) & 15) + f.charAt(a & 15);
        }
        return b;
    }

    function str2rstr_utf8(c) {
        var b = "";
        var d = -1;
        var a, e;
        while (++d < c.length) {
            a = c.charCodeAt(d);
            e = d + 1 < c.length ? c.charCodeAt(d + 1) : 0;
            if (55296 <= a && a <= 56319 && 56320 <= e && e <= 57343) {
                a = 65536 + ((a & 1023) << 10) + (e & 1023);
                d++;
            }
            if (a <= 127) {
                b += String.fromCharCode(a);
            } else {
                if (a <= 2047) {
                    b += String.fromCharCode(192 | ((a >>> 6) & 31), 128 | (a & 63));
                } else {
                    if (a <= 65535) {
                        b += String.fromCharCode(224 | ((a >>> 12) & 15), 128 | ((a >>> 6) & 63), 128 | (a & 63));
                    } else {
                        if (a <= 2097151) {
                            b += String.fromCharCode(240 | ((a >>> 18) & 7), 128 | ((a >>> 12) & 63), 128 | ((a >>> 6) & 63), 128 | (a & 63));
                        }
                    }
                }
            }
        }

        return b;
    }

    function rstr2binl(b) {
        var a = Array(b.length >> 2);
        for (var c = 0; c < a.length; c++) {
            a[c] = 0;
        }
        for (var c = 0; c < b.length * 8; c += 8) {
            a[c >> 5] |= (b.charCodeAt(c / 8) & 255) << (c % 32);
        }
        return a;
    }

    function binl2rstr(b) {
        var a = "";
        for (var c = 0; c < b.length * 32; c += 8) {
            a += String.fromCharCode((b[c >> 5] >>> (c % 32)) & 255);
        }
        return a;
    }

    function binl_md5(p, k) {
        p[k >> 5] |= 128 << ((k) % 32);
        p[(((k + 64) >>> 9) << 4) + 14] = k;
        var o = 1732584193;
        var n = -271733879;
        var m = -1732584194;
        var l = 271733878;
        for (var g = 0; g < p.length; g += 16) {
            var j = o;
            var h = n;
            var f = m;
            var e = l;
            o = md5_ff(o, n, m, l, p[g + 0], 7, -680876936);
            l = md5_ff(l, o, n, m, p[g + 1], 12, -389564586);
            m = md5_ff(m, l, o, n, p[g + 2], 17, 606105819);
            n = md5_ff(n, m, l, o, p[g + 3], 22, -1044525330);
            o = md5_ff(o, n, m, l, p[g + 4], 7, -176418897);
            l = md5_ff(l, o, n, m, p[g + 5], 12, 1200080426);
            m = md5_ff(m, l, o, n, p[g + 6], 17, -1473231341);
            n = md5_ff(n, m, l, o, p[g + 7], 22, -45705983);
            o = md5_ff(o, n, m, l, p[g + 8], 7, 1770035416);
            l = md5_ff(l, o, n, m, p[g + 9], 12, -1958414417);
            m = md5_ff(m, l, o, n, p[g + 10], 17, -42063);
            n = md5_ff(n, m, l, o, p[g + 11], 22, -1990404162);
            o = md5_ff(o, n, m, l, p[g + 12], 7, 1804603682);
            l = md5_ff(l, o, n, m, p[g + 13], 12, -40341101);
            m = md5_ff(m, l, o, n, p[g + 14], 17, -1502002290);
            n = md5_ff(n, m, l, o, p[g + 15], 22, 1236535329);
            o = md5_gg(o, n, m, l, p[g + 1], 5, -165796510);
            l = md5_gg(l, o, n, m, p[g + 6], 9, -1069501632);
            m = md5_gg(m, l, o, n, p[g + 11], 14, 643717713);
            n = md5_gg(n, m, l, o, p[g + 0], 20, -373897302);
            o = md5_gg(o, n, m, l, p[g + 5], 5, -701558691);
            l = md5_gg(l, o, n, m, p[g + 10], 9, 38016083);
            m = md5_gg(m, l, o, n, p[g + 15], 14, -660478335);
            n = md5_gg(n, m, l, o, p[g + 4], 20, -405537848);
            o = md5_gg(o, n, m, l, p[g + 9], 5, 568446438);
            l = md5_gg(l, o, n, m, p[g + 14], 9, -1019803690);
            m = md5_gg(m, l, o, n, p[g + 3], 14, -187363961);
            n = md5_gg(n, m, l, o, p[g + 8], 20, 1163531501);
            o = md5_gg(o, n, m, l, p[g + 13], 5, -1444681467);
            l = md5_gg(l, o, n, m, p[g + 2], 9, -51403784);
            m = md5_gg(m, l, o, n, p[g + 7], 14, 1735328473);
            n = md5_gg(n, m, l, o, p[g + 12], 20, -1926607734);
            o = md5_hh(o, n, m, l, p[g + 5], 4, -378558);
            l = md5_hh(l, o, n, m, p[g + 8], 11, -2022574463);
            m = md5_hh(m, l, o, n, p[g + 11], 16, 1839030562);
            n = md5_hh(n, m, l, o, p[g + 14], 23, -35309556);
            o = md5_hh(o, n, m, l, p[g + 1], 4, -1530992060);
            l = md5_hh(l, o, n, m, p[g + 4], 11, 1272893353);
            m = md5_hh(m, l, o, n, p[g + 7], 16, -155497632);
            n = md5_hh(n, m, l, o, p[g + 10], 23, -1094730640);
            o = md5_hh(o, n, m, l, p[g + 13], 4, 681279174);
            l = md5_hh(l, o, n, m, p[g + 0], 11, -358537222);
            m = md5_hh(m, l, o, n, p[g + 3], 16, -722521979);
            n = md5_hh(n, m, l, o, p[g + 6], 23, 76029189);
            o = md5_hh(o, n, m, l, p[g + 9], 4, -640364487);
            l = md5_hh(l, o, n, m, p[g + 12], 11, -421815835);
            m = md5_hh(m, l, o, n, p[g + 15], 16, 530742520);
            n = md5_hh(n, m, l, o, p[g + 2], 23, -995338651);
            o = md5_ii(o, n, m, l, p[g + 0], 6, -198630844);
            l = md5_ii(l, o, n, m, p[g + 7], 10, 1126891415);
            m = md5_ii(m, l, o, n, p[g + 14], 15, -1416354905);
            n = md5_ii(n, m, l, o, p[g + 5], 21, -57434055);
            o = md5_ii(o, n, m, l, p[g + 12], 6, 1700485571);
            l = md5_ii(l, o, n, m, p[g + 3], 10, -1894986606);
            m = md5_ii(m, l, o, n, p[g + 10], 15, -1051523);
            n = md5_ii(n, m, l, o, p[g + 1], 21, -2054922799);
            o = md5_ii(o, n, m, l, p[g + 8], 6, 1873313359);
            l = md5_ii(l, o, n, m, p[g + 15], 10, -30611744);
            m = md5_ii(m, l, o, n, p[g + 6], 15, -1560198380);
            n = md5_ii(n, m, l, o, p[g + 13], 21, 1309151649);
            o = md5_ii(o, n, m, l, p[g + 4], 6, -145523070);
            l = md5_ii(l, o, n, m, p[g + 11], 10, -1120210379);
            m = md5_ii(m, l, o, n, p[g + 2], 15, 718787259);
            n = md5_ii(n, m, l, o, p[g + 9], 21, -343485551);
            o = safe_add(o, j);
            n = safe_add(n, h);
            m = safe_add(m, f);
            l = safe_add(l, e);
        }
        return Array(o, n, m, l);
    }

    function md5_cmn(h, e, d, c, g, f) {
        return safe_add(bit_rol(safe_add(safe_add(e, h), safe_add(c, f)), g), d);
    }

    function md5_ff(g, f, k, j, e, i, h) {
        return md5_cmn((f & k) | ((~f) & j), g, f, e, i, h);
    }

    function md5_gg(g, f, k, j, e, i, h) {
        return md5_cmn((f & j) | (k & (~j)), g, f, e, i, h);
    }

    function md5_hh(g, f, k, j, e, i, h) {
        return md5_cmn(f ^ k ^ j, g, f, e, i, h);
    }

    function md5_ii(g, f, k, j, e, i, h) {
        return md5_cmn(k ^ (f | (~j)), g, f, e, i, h);
    }

    function safe_add(a, d) {
        var c = (a & 65535) + (d & 65535);
        var b = (a >> 16) + (d >> 16) + (c >> 16);

        return (b << 16) | (c & 65535);
    }

    function bit_rol(a, b) {
        return (a << b) | (a >>> (32 - b));
    }
})

.controller('HomeCtrl', function($scope, $rootScope, $http, $state, $window) {
    $scope.data = {};

    var userJson = {
        'user.id': window.localStorage["sessao.userId"]
    };
    //Monstrar na tela o icone esta certo, falta analisar o porque do erro no if, retornando sempre zero, mesmo estando diferentes!!!
    //=========================================================
    $http({
        method: 'GET',
        url: 'http://192.168.1.5:8080/barterserver/trade/post/list',
        params: userJson
    })
        .success(function(data, status, headers, config) {
            //alert(JSON.stringify(data[0]));
            $scope.items = data;
            //alert("scope "+$scope.items.length);
            //alert("window "+window.localStorage['oldTrades']);
            if ($scope.items.length <= window.localStorage['oldTrades']) {
                $scope.oldNewTrades = 0;
            } else {
                $scope.oldNewTrades = 1;
                window.localStorage['oldTrades'] = null;
                window.localStorage['oldTrades'] = $scope.items.length;
            }
        })
        .error(function(data) {
            alert(data);
        });
    //$scope.oldNewTrades = window.localStorage['oldTradesNum'];

    $scope.pesquisar = function() {
        $rootScope.numfigurinha = $scope.data.numfigurinha;

        if ($rootScope.numfigurinha) {
            $state.go('app.searchresult');
        } else {
            alert("Entre com o numero da figurinha");
        };
    };
})

.controller('MyPicturesCtrl', function($scope, $state, $http, $ionicLoading, $rootScope, $timeout) {
    if (!angular.isDefined(window.localStorage['pictures'])) {
        window.localStorage['pictures'] = angular.toJson([]);
    };

    $scope.items = angular.fromJson(window.localStorage['pictures']);

    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: 'Loading...'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };

    var picJson = {
        'user.id': window.localStorage["sessao.userId"]
    };

    function loadPictures() {
        $scope.show();
        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/user/post/mypictures',
            params: picJson
        })
            .success(function(data, status, headers, config) {
                $scope.items = data;
                window.localStorage['pictures'] = angular.toJson(data);
                $scope.hide();
                $scope.$broadcast('scroll.refreshComplete');
            })
            .error(function(data) {
                $scope.hide();
                $scope.$broadcast('scroll.refreshComplete');
                alert(data.error);
            });
    };

    $scope.doRefresh = loadPictures;
})

.controller('ShowPictureCtrl', function($scope, $state, $stateParams, $http, $rootScope, $ionicLoading, $location) {
    $scope.picId = $stateParams.pictureId;
    $scope.picture = {
        url: ''
    };
    var tmp = angular.fromJson(window.localStorage['pictures']);
    for (var i = 0; i < tmp.length; i++) {
        if (tmp[i].id == $scope.picId) {
            $scope.picture.url = tmp[i].photoURL;
        };
    };

    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: 'Loading...'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };

    $scope.removeItem = function() {
        $scope.show();
        // remover figurinha do banco
        var picJson = {
            'user.id': window.localStorage["sessao.userId"],
            'picture.id': $scope.picId
        };
        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/user/picture/delete',
            params: picJson
        })
            .success(function(data, status, headers, config) {
                $scope.list = [];
                for (var i = 0; i < tmp.length; i++) {
                    if (tmp[i] != $scope.picId) {
                        $scope.list.push(tmp[i]);
                    };
                };
                window.localStorage['pictures'] = angular.toJson($scope.list);
                $scope.hide();
                alert('Figurinha removida com sucesso!');
                $location.path('app.mypictures');
            })
            .error(function(data) {
                $scope.hide();
                alert('Erro na remoção' + data);
            });
    };
})

.controller('ChatCtrl', function($scope, $rootScope, $http, $ionicLoading, $interval, $ionicScrollDelegate, $ionicSideMenuDelegate) {
    // ************************************************************ //
    $scope.chatKey = "chat" + $rootScope.tradeId;
    var stopTime;
    $scope.messages = angular.fromJson(window.localStorage[$scope.chatKey]);
    $scope.dados = {};
    $scope.username = window.localStorage['sessao.name'];
    // fim iniciaçização
    // ************************************************************ //

    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: 'Loading...'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };

    /* SEND MESSAGE */
    $scope.sendMessage = function() {
        if ($scope.dados.message != "") {
            var msgJson = {
                'chat.user.id': window.localStorage["sessao.userId"],
                'chat.trade.id': $rootScope.tradeId,
                'chat.message': $scope.dados.message
            };
            $http({
                method: 'POST',
                url: 'http://192.168.1.5:8080/barterserver/user/new/message',
                params: msgJson
            })
                .success(function() {
                    loadChat();
                    $ionicScrollDelegate.scrollBottom();
                })
                .error(function() {
                    alert('Mensagem não pode ser enviada!');
                });
            $scope.dados.message = "";
        };
    };

    function loadChat() {
        var chatJson = {
            'trade.id': $rootScope.tradeId,
            'user.id': window.localStorage["sessao.userId"]
        };
        //  @barterserver/user/chat
        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/user/chat',
            params: chatJson
        })
            .success(function(data, status, headers, config) {
                var tmp = angular.toJson(data);
                if (window.localStorage[$scope.chatKey] != tmp) {
                    window.localStorage[$scope.chatKey] =
                        $scope.messages = data;
                    $ionicScrollDelegate.scrollBottom();
                } else {
                    $scope.messages = data;
                    $ionicScrollDelegate.scrollBottom();
                };

            })
            .error(function(data, status, headers, config) {
                alert('Sorry, Unable to reach chat server' + data);
                $state.go('app.home');
            });
    };

    $scope.$on('$destroy', function() {
        if (angular.isDefined(stopTime)) {
            $interval.cancel(stopTime);
            stopTime = undefined;
        };
    });

    function init() { // inicialização do chat
        $ionicSideMenuDelegate.toggleRight($scope.$$childHead);
        if (angular.isDefined(stopTime)) return;
        stopTime = $interval(loadChat, 20000);
    };

    init();
})

.controller('RegisterPicturerCtrl', function($scope, $rootScope, $state, $http, $ionicLoading, $location) {
    $scope.data = {}; // inicialização

    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: '<i class="icon ion-looping loading-icon"></i><br/>Carregando...',
            animation: 'fade-in'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };
    $scope.cancel = function() {
        $location.path("/app/mypictures");
    };
    $scope.onSuccess = function(imageURI) {
        $scope.show();
        var image = document.getElementById('imageId');
        image.src = imageURI;
        $scope.picData = imageURI;
        $scope.hide();
    };

    $scope.onFail = function(message) {
        alert('Failed because: ' + message);
    };

    $scope.captureImage = function() {
        var options = {
            quality: 80,
            destinationType: Camera.DestinationType.FILE_URI,
            sourceType: Camera.PictureSourceType.CAMERA,
            allowEdit: false,
            encodingType: Camera.EncodingType.JPEG,
            targetWidth: 500,
            targetHeight: 600,
            saveToPhotoAlbum: true
        };        
        navigator.camera.getPicture($scope.onSuccess, $scope.onFail, options);
    };

    $scope.onUploadSucess = function() {
        var picJson = {
            'picture.id': $scope.ActualPicId,
            'picture.photoURL': "http://192.168.1.5:8080/barterserver/pictures/" + $scope.ActualPicId + ".jpg"
        };
        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/user/post/picture/add',
            params: picJson
        })
            .success(function(data, status, headers, config) {
                $scope.hide();
                alert("Cadastro efetuado com sucesso");
                $location.path('/app/mypictures');
            })
            .error(function(data) {
                $scope.hide();
                alert("Upload error");
            });
        delete $scope.ActualPicId;
    };

    $scope.onUploadFail = function(evt) {
        $scope.hide();
        alert('Falha no Upload: ' + evt);
    };

    // upload picture
    $scope.send = function() {
        var tmp = document.getElementById('imageId');
        if (tmp.src) {
            $scope.show();
            var prodData = $scope.data;
            var picJson = {
                'user.id': window.localStorage["sessao.userId"],
                'picture.title': prodData.productName
            };
            $http({
                method: 'GET',
                url: 'http://192.168.1.5:8080/barterserver/user/post/picture/add',
                params: picJson
            })
                .success(function(data, status, headers, config) {
                    var image = $scope.picData;
                    $scope.ActualPicId = data.id;
                    var options = new FileUploadOptions();
                    options.fileKey = "file";
                    options.mineType = "image/jpeg";
                    options.fileName = data.id + ".jpg";
                    options.chunckedMode = false;
                    options.correctOrientation = true;
                    var params = new Object();
                    options.params = params;
                    var ft = new FileTransfer();
                    ft.upload(image, encodeURI("http://192.168.1.5:8080/barterserver/upload.jsp"), $scope.onUploadSucess, $scope.onUploadFail, options);
                })
                .error(function() {
                    $scope.hide();
                    alert("Upload error");
                });
        } else {
            alert('Campo Foto inválido');
        };
    }; //end upload file
})

.controller('searchResultCtrl', function($scope, $rootScope, $http) {
    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: 'Loading...'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };

    //alert("Num: "+$rootScope.numfigurinha);
    //alert("User: "+window.localStorage["sessao.userId"]);

    $scope.ownerName = $rootScope.userReqName;

    var userJson = {
        'title': $rootScope.numfigurinha,
        'currentUser.id': window.localStorage["sessao.userId"]
    };

    $http({
        method: 'GET',
        url: 'http://192.168.1.5:8080/barterserver/search',
        params: userJson
    })
        .success(function(data, status, headers, config) {
            //alert(JSON.stringify(data[0])); 
            $scope.items = data;

        })
        .error(function(data, status, headers, config) {
            alert("Erro!!!");
        });


    $scope.match = function(pictureId, ownerId) {
        var pId = pictureId;
        var oId = ownerId;

        //alert("P.Id: "+pictureId);
        //alert("OW.id: "+ownerId);

        var picJson = {
            'trade.pictureOffering.id': pictureId,
            'trade.userOffering.id': ownerId,
            'trade.userRequiring.id': window.localStorage["sessao.userId"]
        };

        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/trade/post/new',
            params: picJson
        }).
        success(function(data, status, headers, config) {
            //alert("Trade Lançado");
            $state.go('app.home');
        }).
        error(function(data, status, headers, config) {
            alert("Erro ao dar match!");
        });
    }
})

.controller('userPicturesCtrl', function($scope, $rootScope, $http, $state) {
    $scope.show = function() {
        $scope.loading = $ionicLoading.show({
            content: 'Loading...'
        });
    };

    $scope.hide = function() {
        $scope.loading.hide();
    };

    $scope.ownerName = $rootScope.userReqName;
    var userJson = {
        'user.id': $rootScope.userReqId
    };

    $http({
        method: 'GET',
        url: 'http://192.168.1.5:8080/barterserver/user/post/mypictures',
        params: userJson
    }).
    success(function(data, status, headers, config) {
        //alert("Certo!!!!!!");
        $scope.items = data;

    }).
    error(function(data, status, headers, config) {
        alert("Erro!!!");
    });

    $scope.match = function(pictureId) {

        //alert("P.Id: "+pictureId);
        //alert("T.Id: "+$rootScope.tradeId);

        var picJson = {
            'trade.id': $rootScope.tradeId,
            'trade.pictureRequiring.id': pictureId
        };

        $http({
            method: 'GET',
            url: 'http://192.168.1.5:8080/barterserver/trade/post/update',
            params: picJson
        }).
        success(function(data, status, headers, config) {
            //alert("Lançado trade!!!");
            $state.go('app.home');
        }).
        error(function(data, status, headers, config) {
            alert("Erro ao dar match!!!");
        });
    }
})

.controller('categoriesCtrl', function($scope, $rootScope, $http, $state, $window) {
        
});



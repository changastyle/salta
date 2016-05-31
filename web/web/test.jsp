<%-- 
    Document   : test
    Created on : 31/05/2016, 10:04:50
    Author     : nico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%@include file="common.jsp"%>
    </head>
    <bod0 ng-app="app" ng-controller="test">
        <h1></h1>
        
        <div id="lista{{b4}}" class="listas col-xs-3" ng-repeat="b4 in bucle4">
            
            <div class="encabezadosLista" ng-click="open('lista'+ b4)">
                <h3 class="htresEncabezadoListas">{{juegos.extractos}}</h3>
            </div>

            <!-- ITEM FIJO -->
            <div id="item1lista1" class="itemsDeListas col-xs-12">
                <div class="indice col-xs-3" id="indice1">{{juegos[b4].extractos[0].posic}}</div>
                <div class="numero col-xs-9" id="numero1">{{juegos[b4].extractos[0].premio}}</div>
            </div>

            <!-- ITEM -->
            <div id="item1lista{{b+indiceVariable+1}}" class="itemsDeListas col-xs-12" ng-repeat="b in bucle5">
                <div class="indice col-xs-3" id="indice{{b + indiceVariable + 1}}">{{b + indiceVariable + 1}}</div>
                <div class="numero col-xs-9" id="numero{{b + indiceVariable + 1}}">{{juegos[b4].extractos[b+indiceVariable].premio}}</div>
            </div>
        </div>
    </body>
    <script>
        app = angular.module('app', []);
    
        app.controller('test', function ($scope, $http, $interval)
        {
            $scope.bucle4 = new Array(0,1,2,3);
            $scope.bucle5 = new Array(1,2,3,4);


            $scope.findJuegos = function()
            {
                $.ajax
                ({
                    url:"../proceso",
                    success: function (resultado, textStatus, jqXHR)
                    {
                        $scope.juegos = resultado;
                        console.log("juegos:" + JSON.stringify($scope.juegos));

                        $scope.$apply();
                    }
                }); 
            }
            $scope.findJuegos();
        });     
    </script>
</html>

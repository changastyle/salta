<%-- 
    Document   : import
    Created on : Apr 18, 2016, 5:45:09 PM
    Author     : NicoDesktop
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="common.jsp"%>
        <link rel="stylesheet" href="res/css/listas.css">
    </head>
    <body ng-app="app" ng-controller="monitor">
        
        <!--<div class="col-xs-10 col-xs-offset-1">-->
        <div class="contenedor">
            
            <!-- BANNER: -->
            <div class="banner">
                <div id="subBanner" class="col-xs-6 col-xs-offset-3">
                    <img class="col-xs-2 col-md-2 col-md-offset-2 img img-responsive imgBanner" src="res/img/loteria.jpg">
                    <div class="col-xs-7 col-xs-offset-1 txtBanner" >
                        <h3 class="htresBanner  vertical-center">LOTERÍA DE SALTA</h3>
                    </div>
                </div>
            </div>
            
            <!-- SUB BANNER-->
            <div class="banner2">
                <h3 class="htresBanner2">Sorteo Matutina del Martes 19 Abril 2016</h3>
            </div>
            
            <!-- PRINCIPAL -->
            <div id="contenidoPrincipal" class="contenidoPrincipal col-xs-12" >
                
                <div id="lista{{b4}}" class="listas col-xs-3" ng-repeat="b4 in bucle4">
                    <div class="encabezadosLista" ng-click="open('lista'+ b4)">
                        <h3 class="htresEncabezadoListas">{{juegos[b4].nombre}}</h3>
                    </div>
                    
                    <!-- ITEM -->
                    <div id="item1lista1" class="itemsDeListas col-xs-12" ng-repeat="b in bucle5">
                        <div class="indice col-xs-3" id="indice{{b + indiceVariable + 1}}">{{b + indiceVariable + 1}}</div>
                        <div class="numero col-xs-9" id="numero{{b + indiceVariable + 1}}">{{juegos[b4].numeros[b+indiceVariable]}}</div>
                    </div>
                </div>
            </div>
            
            <!-- CHANGUITA:-->
            <div id="changuita" class="col-xs-12" hidden>
                <div class="listaChanguita" ng-repeat="c in changuita">
                    {{c}}
                </div>
            </div>
            
            <!-- FOOTER: -->
            <div class="footer col-xs-12">
                <!--<img src="res/img/logoTecno.gif">-->
                <h3 style="text-align: center;color:white">Tecno Accion S.A 2016</h3>
            </div>
        </div>
           
        
    </body>
    <script src="res/js/controller.js"></script>
</html>

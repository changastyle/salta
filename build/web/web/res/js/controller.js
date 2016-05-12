    var bandera = false;
    app = angular.module('app', []);

    app.controller('monitor', function ($scope, $http, $interval)
    {
        $scope.juegos = [];
        $scope.bucle5 = new Array(1,2,3,4);
        $scope.bucle5Changuita = new Array(0,1,2,3,4);
        $scope.bucle4 = new Array(0,1,2,3);
        $scope.tiempoIntervalo = 3* 1000;   //ESPERA EN MICRO-SEGUNDOS.
        $scope.indiceVariable = 0;
        $scope.changuita = [5555,4444,5555,4444,6666,5555,4444,5555,4444,6666,5555,4444,5555,4444,6666,5555,4444,5555,4444,6666];
       
       $scope.findJuegos = function()
       {
            $.ajax
            ({
                url:"../juegos",
                success: function (resultado, textStatus, jqXHR)
                {
                    $scope.juegos = resultado;
                    console.log("resultado:" + JSON.stringify($scope.juegos));

                    $scope.$apply();
                }
            }); 
       }
       $scope.open = function(lista)
       {
           clasePeque = "col-xs-3";
           claseHuge = "col-xs-12";
           
          console.log("open: " + $("#" + lista).hasClass(clasePeque));
           
           if($("#" + lista).hasClass(clasePeque))
           {
                $(".listas").each(function(index,element)
                {
                    //console.log("esto:" + JSON.stringify($(element)) + " | " + $(lista));
                    console.log("attr:" + lista);
                    if($(element).attr('id') != lista )
                    {
                        $(element).hide("fast");
                    }
                });
                $("#" + lista).removeClass(clasePeque);
                $("#" + lista).addClass("huge");
                $("#" + lista).addClass(claseHuge); 
           }
           else
           {
                $(".listas").each(function(index,element)
                {
                    $(element).show("fast");
                });
                $("#" + lista).removeClass(claseHuge);
                $("#" + lista).addClass(clasePeque);
                $("#" + lista).removeClass("huge");
           }
       }
       $scope.mostrarChanguita = function()
       {
           console.log("mostrando changuita");
           $(".contenidoPrincipal").hide("slow",function()
           {
                $("#changuita").show("slow");
           });
       }
       $scope.ocultarChanguita = function()
       {
           console.log("ocultando changuita");
           $("#changuita").hide("slow",function()
           {
               $(".contenidoPrincipal").show("slow");
           });
           
       }
       $scope.findChanguita = function()
       {
           $.ajax
           (
                {
                    url:"../changuita",
                    success: function (resultado, textStatus, jqXHR)
                    {
                        console.log("findChanguita:" + JSON.stringify(resultado));
                        $scope.changuita = resultado;
                        $scope.$apply();
                    }
                }
           );
       }
       $scope.findFecha = function()
       {
           $.ajax
           (
                {
                    url:"../fecha",
                    success: function (resultado, textStatus, jqXHR)
                    {
                        console.log("fecha:" + JSON.stringify(resultado));
                        $scope.fecha = resultado;
                        $scope.$apply();
                    }
                }
           );
       }
      
       $scope.findJuegos();
       $scope.findChanguita();
       $scope.findFecha();
       //EJECUTANDO EL MOVIMIENTO DE LOS NUMEROS CADA CIERTO INTERVALO DE TIEMPO:
       setInterval(function()
       {
           console.log("espero tiempo inicial " + $scope.tiempoIntervalo + " | " + $scope.indiceVariable);
           if ($scope.indiceVariable < 14)
           {
                $scope.indiceVariable += 4;
                $(".itemsDeListas").each(function(index,element)
                {
                    //$(element).hide("slow");
                    //console.log($(element).attr('id'));
                    if($(element).attr('id') != "item1lista1")
                    {
                        $(element).addClass('animated');
                        $(element).addClass('flipInX');
                         setTimeout(function()
                        {
                            $(element).removeClass('animated');
                            $(element).removeClass('flipInX');
                        },1000);
                    }
                    
                });
                /*
                $("#contenidoPrincipal").hide("slow",function()
                {
                    $("#contenidoPrincipal").show("slow");
                });*/
           }
           else if($scope.indiceVariable >= 16)
           {
                console.log("changita visible : " + $("#changuita").css('display'));
                
                if( $("#changuita").css('display') == "none")
                {
                    $scope.mostrarChanguita();
                }
                else
                {
                    $scope.ocultarChanguita();
                    $scope.indiceVariable = 0;
                }
           }
           
           //$scope.$digest();
           $scope.$apply();
       },
        $scope.tiempoIntervalo);
    });
       /*setInterval(function()
       {
           console.log("espero tiempo inicial " + $scope.tiempoInicial);
           if ($scope.indiceVariable < 14)
           {
                $scope.indiceVariable += 5;
                $("#contenidoPrincipal").hide("slow",function()
                {
                    $("#contenidoPrincipal").show("slow");
                });
           }
           else if($scope.indiceVariable == 15)
           {
                console.log("changita visible : " + $("#changuita").css('display'));
                
                if( $("#changuita").css('display') == "none")
                {
                    $scope.mostrarChanguita();
                }
                else
                {
                    $scope.ocultarChanguita();
                    $scope.indiceVariable = 0;
                }
           }
           
           //$scope.$digest();
           $scope.$apply();
       },
       $scope.tiempoInicial);
    });*/
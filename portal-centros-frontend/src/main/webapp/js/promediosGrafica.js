
function buildChart(pAverageCenter,pAverageNational){
    // Descomponer los promedios

    var jsonAverageCenter = "";
    var jsonAverageNational = "";
    var show = false;
    try {
        jsonAverageCenter = JSON.parse(pAverageCenter);
        jsonAverageNational = JSON.parse(pAverageNational);
        if (jsonAverageCenter.length > 0 && jsonAverageNational.length > 0)
            show = true;
    } catch(e) {
        console.log("Sin registros al momento de generar el gráfico" + e); 
    }


    if (show){
       $("#emptyChart").hide(); 
       jsonAverageCenter.sort(function (a, b) {
          if (a.nombreComponente > b.nombreComponente) {
            return 1;
          }
          if (a.nombreComponente < b.nombreComponente) {
            return -1;
          }
          return 0;
        });
        // Obtener componentes.
        var uniqueComponent = [...new Set(jsonAverageCenter.map(item => item.nombreComponente))];
        // Obtener promedios.
        var uniquePromInst = [...new Set(jsonAverageCenter.map(item => item.promedioCalificacion))];
        var avgComponentNational = jsonAverageNational.filter(function(item)	{
            return uniqueComponent.indexOf(item.nombreComponente) > -1;
        });

        avgComponentNational.sort(function (a, b) {
            if (a.nombreComponente > b.nombreComponente) {
                return 1;
            }
            if (a.nombreComponente < b.nombreComponente) {
                return -1;
            }
            return 0;
        });

        var uniquePromNat = [...new Set(avgComponentNational.map(item => item.promedioCalificacion))];

        var data = {
            labels: uniqueComponent,
            series: [
            {
              label: 'Promedio sede educativa',
              values: uniquePromInst
            },
            {
              label: 'Promedio nacional',
              values: uniquePromNat
            }]
        };

        var chartWidth       = 300,
            barHeight        = 30,
            groupHeight      = barHeight * data.series.length,
            gapBetweenGroups = 15,
            spaceForLabels   = 250,
            spaceForLegend   = 200;

        var dataAvg = [];
        for (var i=0; i<data.labels.length; i++) {
            for (var j=0; j<data.series.length; j++) {
                dataAvg.push(data.series[j].values[i]);
            }
        }

        // Color de escalas
        var color = d3.scaleOrdinal()
                     .range(["#29ABE2", "#FF7400"]);

        var chartHeight = barHeight * dataAvg.length + gapBetweenGroups * data.labels.length;

        var x = d3.scaleLinear()
            .domain([0, 10.0])
            .range([0, chartWidth]);

        var y = d3.scaleLinear()
            .range([chartHeight + gapBetweenGroups, 0]);

        var yAxis = d3.axisLeft()
            .scale(y)
            .tickFormat('')
            .tickSize(0);

        var xAxis = d3.axisBottom(x).tickFormat(function(d){ 
        return d;
        });

        // Especificar el área del gráfico 
        var chart = d3.select(".chart")
            .attr("width", spaceForLabels + chartWidth + spaceForLegend)
            .attr("height", chartHeight +30);

        // Crear barras
        var bar = chart.selectAll("g")
            .data(dataAvg)
            .enter().append("g")
            .attr("transform", function(d, i) {
                return "translate(" + spaceForLabels + "," + (i * barHeight + gapBetweenGroups * (0.5 + Math.floor(i/data.series.length))) + ")";
            });

        // Ancho de rectángulos
        bar.append("rect")
            .attr("fill", function(d,i) { return color(i % data.series.length); })
            .attr("class", "bar")
            .attr("width", x)
            .attr("height", barHeight - 1);

        // Agregar label en la barra (nota promedio)
        bar.append("text")
            .attr("x", function(d) { return x((isNaN(d) || d === null) ? 0.0 : d) + 5; })
            .attr("y", barHeight / 2)
            .attr("fill", "#d0743c")
            .attr("dy", ".35em")
            .text(function(d) { return (isNaN(d) || d === null) ? 0.00 : d.toFixed(2); }); 

        // Digujar leyendas
        bar.append("text")
            .attr("class", "label")
            .attr("x", function(d) { return - 15; })
            .attr("y", groupHeight / 2)
            .attr("dy", ".35em")
            .text(function(d,i) {
                if (i % data.series.length === 0)
                    return data.labels[Math.floor(i/data.series.length)];
                else
                    return ""
            });

        chart.append("g")
            .attr("class", "y axis")
            .attr("transform", "translate(" + spaceForLabels + ", " + -gapBetweenGroups/2 + ")")
            .call(yAxis);

         chart.append("g")         // X Axis
            .attr("class", "x axis")
            .attr("transform", "translate(" + spaceForLabels + "," + chartHeight + ")")
            .call(xAxis);



        // Dibujar leyenda
        var legendRectSize = 18,
            legendSpacing  = 4;

        var legend = chart.selectAll('.legend')
            .data(data.series)
            .enter()
            .append('g')
            .attr('transform', function (d, i) {
                var height = legendRectSize + legendSpacing;
                var offset = -gapBetweenGroups/2;
                var horz = spaceForLabels + chartWidth + 40 - legendRectSize;
                var vert = i * height - offset;
                return 'translate(' + horz + ',' + vert + ')';
            });

        legend.append('rect')
            .attr('width', legendRectSize)
            .attr('height', legendRectSize)
            .style('fill', function (d, i) { return color(i); })
            .style('stroke', function (d, i) { return color(i); });

        legend.append('text')
            .attr('class', 'legend')
            .attr('x', legendRectSize + legendSpacing)
            .attr('y', legendRectSize - legendSpacing)
            .text(function (d) { return d.label; });
    }   
    else {
        $("#emptyChart").show();
    }
} 
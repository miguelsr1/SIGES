
    console.log("datos califica",datosCalificaciones);
    var randomColor = (function(){
  var golden_ratio_conjugate = 0.618033988749895;
  var h = Math.random();

  var hslToRgb = function (h, s, l){
      var r, g, b;

      if(s == 0){
          r = g = b = l; // achromatic
      }else{
          function hue2rgb(p, q, t){
              if(t < 0) t += 1;
              if(t > 1) t -= 1;
              if(t < 1/6) return p + (q - p) * 6 * t;
              if(t < 1/2) return q;
              if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
              return p;
          }

          var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
          var p = 2 * l - q;
          r = hue2rgb(p, q, h + 1/3);
          g = hue2rgb(p, q, h);
          b = hue2rgb(p, q, h - 1/3);
      }

      return '#'+Math.round(r * 255).toString(16)+Math.round(g * 255).toString(16)+Math.round(b * 255).toString(16);
  };
  
  return function(){
    h += golden_ratio_conjugate;
    h %= 1;
    return hslToRgb(h, 0.5, 0.60);
  };
})();
    
    var init = [
    {"year": "2017", "name": "Matemática", "value": 6.5},
    {"year": "2017", "name": "Inglés","value": 7.4},
    {"year": "2017", "name": "Ciencias", "value": 8.3},
    {"year": "2017", "name": "Lenguaje y literatura", "value": 5},
    {"year": "2018", "name": "Matemática", "value": 7.5},
    {"year": "2018", "name": "Inglés","value": 8.4},
    {"year": "2018", "name": "Ciencias", "value": 8.1},
    {"year": "2018", "name": "Lenguaje y literatura", "value": 6},
];

chart(init)

function chart(result) {

	var format = d3.format(",.0f")

	var years = [...new Set(result.map(d => d.year))]
	var fruit = [...new Set(result.map(d => d.name))]

	var options = d3.select("#year").selectAll("option")
		.data(years)
	.enter().append("option")
		.text(d => d)

	var svg = d3.select("#grafica"),
		margin = {top: 25, bottom: 10, left: 50, right: 45},
		width = +svg.attr("width") - margin.left - margin.right,
		height = +svg.attr("height") - margin.top - margin.bottom;

	var x = d3.scaleLinear()
		.range([margin.left, width - margin.right])
	
	var y = d3.scaleBand()
		.range([margin.top, height - margin.bottom])
		.padding(0.1)
		.paddingOuter(0.5)
		.paddingInner(0.5)

	var xAxis = svg.append("g")
		.attr("class", "x-axis")
		.attr("transform", `translate(0,${margin.top})`)

	var yAxis = svg.append("g")
		.attr("class", "y-axis")
		.attr("transform", `translate(${margin.left},0)`)

	update(d3.select("#year").property("value"), 750, 250)

	function update(input, speed, delay) {

		var data = result.filter(f => f.year == input)

		var sum = d3.sum(data, d => d.value)

		x.domain([0, 10.0]).nice()

		svg.selectAll(".x-axis").transition().duration(speed)
			.call(d3.axisTop(x).tickSizeOuter(0));
			
		data.sort((a, b) => b.value - a.value)

		y.domain(data.map(d => d.name))

		svg.selectAll(".y-axis").transition().duration(speed)
			.call(d3.axisLeft(y));

		yAxis.selectAll("text").remove()
		yAxis.selectAll("line").remove()

		var bar = svg.selectAll(".bar")
			.data(data, d => d.name)

		bar.exit().remove();

		bar.enter().insert("g", ".y-axis").append("rect")
			.attr("class", "bar")
			.attr("fill", randomColor)
			.attr("x", x(0))
			.attr("y", d => y(d.name))
			.attr("height", y.bandwidth())
			.merge(bar)
		.transition().duration(speed)
			.delay((_, i) => delay * i)
			.attr("y", d => y(d.name))
			.attr("width", d => x(d.value) - x(0));

		var value = svg.selectAll(".value")
			.data(data, d => d.name)

		value.exit().remove();

		value.enter().append("text")
			.attr("class", "value")
			.attr("opacity", 0)
			.attr("dy", 4)
			.attr("y", d => y(d.name) + y.bandwidth() / 2)
			.merge(value)
		.transition().duration(speed)
			.delay((_, i) => delay * i)
			.attr("opacity", 1)
			.attr("y", d => y(d.name) + y.bandwidth() / 2)
			.attr("x", d =>  x(d.value) + 5)
                        .text(d => d.value)
			//.text(d => format((d.value / sum) * 100) + " %")

		var name = svg.selectAll(".name")
			.data(data, d => d.name)

		name.exit().remove();

		name.enter().append("text")
			.attr("class", "name")
			.attr("opacity", 0)
			.attr("dy", -5)
			.attr("y", d => y(d.name))
			.merge(name)
		.transition().duration(speed)
			.delay((_, i) => delay * i)
			.attr("opacity", 1)
			.attr("y", d => y(d.name))
			.attr("x", d =>  x(0) + 5)
			.text(d => d.name)
	}
        var select = d3.select("#year")
		.style("border-radius", "5px")
		.on("change", function() {
			update(this.value, 750, 0)
		})
}
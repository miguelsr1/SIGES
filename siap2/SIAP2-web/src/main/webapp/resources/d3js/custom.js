

//var rectW = 100;
//var    rectH = 60;
///////
//var width = 900;
//var height = 1100;
//var maxLabel = 150;
//var duration = 500;
//var radius = 5;
//    
//var i = 0;
//var root;

var tree = d3.layout.tree()
    .size([height, width]);

var diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.y + rectW / 2, d.x +  rectH / 2]; });

var svg = d3.select("#tree").append("svg")
    .attr("width", width)
    .attr("height", height)
  .append("svg:g")
    .attr("class","drawarea")
  .append("svg")
        .append("g")
        .attr("transform", "translate(" + 100 + "," + -(rectH / 2)+ ")" );



//root.children.forEach(collapse);

function update(source) 
{
    // Compute the new tree layout.
    var nodes = tree.nodes(root).reverse();
    var links = tree.links(nodes);

    // Normalize for fixed-depth.
    nodes.forEach(function(d) { d.y = d.depth * 240; });

    // Update the nodes…
    var node = svg.selectAll("g.node")
        .data(nodes, function(d){ 
            return d.id || (d.id = ++i); 
        });

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter()
        .append("g")
        .attr("class", "node")
        .attr("transform", function(d){ return "translate(" + source.y0 + "," + source.x0 + ")"; })
        .on("click", click);




  nodeEnter.append("rect")
            //.attr("x", rectW )
            //.attr("y", rectH)
            .attr("width", rectW)
            .attr("height", rectH)
            .attr("stroke", "black")
            .attr("stroke-width", 0.5) 
            .style("fill", function(d){ 
                return d.color ? d.color : "#ecf1f6";
                //return d._children ? "lightsteelblue" : "white"; 
            });


    nodeEnter.append("foreignObject")
            .attr("class",  function(d){ return  " " +d.cssClassNode; })
            //.attr("x", -rectW / 2)
            //.attr("y", -rectH / 2)
            .attr("width",rectW)
            .attr("height",rectH)
            .append("xhtml:div")
                .attr("x", -rectW / 2)
                .attr("y", -rectH / 2)
                .attr("dy", ".35em")
                .attr("text-anchor", "middle")
                .html(function(d){ return d.name; });
        //.html("FIRST LINE <br> <b>SECOND LINE</b>");




/*

    nodeEnter.append("circle")
        .attr("r", 0)
        .style("fill", function(d){ 
            return d._children ? "lightsteelblue" : "white"; 
        });

    nodeEnter.append("text")
        .attr("x", function(d){ 
            var spacing = computeRadius(d) + 5;
            return d.children || d._children ? -spacing : spacing; 
        })
        .attr("dy", "3")
        .attr("text-anchor", function(d){ return d.children || d._children ? "end" : "start"; })
        .text(function(d){ return d.name; })
        .style("fill-opacity", 0);


*/








    // Transition nodes to their new position.
    
    var nodeUpdate = node.transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

    nodeUpdate.select("rect")
        .attr("width", rectW)
        .attr("height", rectH)
        .attr("stroke", "black")
        .attr("stroke-width", 0.5)
        .style("fill", function (d) {
        //return d._children ? "lightsteelblue" : "#fff";
         return d.color ? d.color : "#ecf1f6";
    });

    nodeUpdate.select("text").style("fill-opacity", 1);




    // Transition exiting nodes to the parent's new position.
    var nodeExit = node.exit().transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
        .remove();


    nodeExit.select("rect")
        .attr("width", rectW)
        .attr("height", rectH)
    //.attr("width", bbox.getBBox().width)""
    //.attr("height", bbox.getBBox().height)
    .attr("stroke", "black")
        .attr("stroke-width", 1);

    nodeExit.select("text").style("fill-opacity", 0);




    // Update the links…
    var link = svg.selectAll("path.link")
        .data(links, function(d){ return d.target.id; });

    // Enter any new links at the parent's previous position.
//    link.enter().insert("path", "g")
//        .attr("class", "link")
//        .attr("d", function(d){
//            var o = {x: source.x0, y: source.y0 };
//            return diagonal({source: o, target: o});
//        });

    // Enter any new links at the parent's previous position.
    link.enter().insert("path", "g")
        .attr("class", "link")
        .attr("x", rectW / 2)
        .attr("y", rectH / 2)
        .attr("d", function (d) {
        var o = {
            x: source.x0,
            y: source.y0
        };
        return diagonal({
            source: o,
            target: o
        });
    });


    // Transition links to their new position.
    link.transition()
        .duration(duration)
        .attr("d", diagonal);

    // Transition exiting nodes to the parent's new position.
    link.exit().transition()
        .duration(duration)
        .attr("d", function(d){
            var o = {x: source.x , y: source.y};
            return diagonal({source: o, target: o});
        })
        .remove();

    // Stash the old positions for transition.
    nodes.forEach(function(d){
        d.x0 = d.x;
        d.y0 = d.y;
    });
    
    // se agrega la funcion de zoom
    d3.select("svg")
        .call(d3.behavior.zoom()
              .scaleExtent([0.5, 5])
              .on("zoom", zoom));
}

function zoom() {
    var scale = d3.event.scale,
        translation = d3.event.translate,
        tbound = -height * scale,
        bbound = height * scale,
        lbound = (-width + 0) * scale,
        rbound = (width - 100) * scale;
    // limit translation to thresholds
    translation = [
        Math.max(Math.min(translation[0], rbound), lbound),
        Math.max(Math.min(translation[1], bbound), tbound)
    ];
    d3.select(".drawarea")
        .attr("transform", "translate(" + translation + ")" +
              " scale(" + scale + ")");
}
//function computeRadius(d)
//{
//    if(d.children || d._children) return radius + (radius * nbEndNodes(d) / 10);
//    else return radius;
//}

function nbEndNodes(n){
    nb = 0;    
    if(n.children){
        n.children.forEach(function(c){ 
            nb += nbEndNodes(c); 
        });
    }
    else if(n._children){
        n._children.forEach(function(c){ 
            nb += nbEndNodes(c); 
        });
    }
    else nb++;
    
    return nb;
}

function click(d){
    if (d.children){
        d._children = d.children;
        d.children = null;
    } 
    else{
        d.children = d._children;
        d._children = null;
    }
    update(d);
}

function collapse(d){
    if (d.children){
        d._children = d.children;
        d._children.forEach(collapse);
        d.children = null;
    }
}


 update(root);








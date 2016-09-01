var RENDERER = {};
RENDERER.render = function(generateTable) {
    $("body svg").remove()


    var nodes = APP.nodes;
    var links = APP.links

    APP.txt = {}
    for (var i in APP.textsArray) {
        var txt = APP.textsArray[i]
        APP.txt[txt['_1']] = txt['_2']
    }
    if (generateTable) {
        GEN.generateSentancesTable();
    }
    var width = $(window).width()*0.7;
    var height = $(window).height();
    var force = d3.layout.force()
        .nodes(d3.values(nodes))
        .links(links)
        .size([width, height])
        .linkDistance(150)
        .charge(-500)
        .on("tick", tick)
        .start();


    var svg = d3.select("#svg_container").append("svg").classed("structure_svg", true)
        .attr("width", width)
        .attr("height", height);

    var circle = svg.append("g").selectAll("circle")
        .data(force.nodes())
        .enter().append("circle")
        .attr("r", 9)
        .attr("class", function (d) {
            var classes = ""
            for (var i in d.snts) {
                classes += "sent_" + d.snts[i] + " "
            }
            return classes;
        }).style("fill", function (d) {
            var rgb = (0x100 + d.sent * 20).toString(16)
            return "#" + rgb;
        })
        .call(force.drag)
        .on("mouseover", function (d) {
            $('tr[data-sent="' + d.sent + '"]').toggleClass("snet_row_high");
            $('.sent_' + d.sent).toggleClass("node_high");
            $('.text_sent_' + d.sent).toggleClass("text_high");
            $('.link_text_' + d.sent).toggleClass("text_high");
        }).on("mouseout", function (d) {
            $('tr[data-sent="' + d.sent + '"]').toggleClass("snet_row_high");
            $('.sent_' + d.sent).toggleClass("node_high");
            $('.text_sent_' + d.sent).toggleClass("text_high");
            $('.link_text_' + d.sent).toggleClass("text_high");
        })

    svg.append("defs").selectAll("marker")
        .data(["extends", "implementing", "field"])
        .enter().append("marker")
        .attr("id", function (d) {
            return d;
        })
        .attr("viewBox", "-10 -10 20 20")
        .attr("refX", 40)
        .attr("refY", -1.5)
        .attr("markerWidth", 6)
        .attr("markerHeight", 6)
        .attr("orient", "auto")
        .append("path")
        .style("fill", "#d3790f")
        .style("stroke", "#d3790f")
        .attr("d", "M 0,0 m -10,-10 L 10,0 L -10,10 Z");


    var path = svg.append("g").selectAll("path")
        .data(force.links())
        .enter().append("path")
        .attr("class", function (d) {
            return "link " + d.linkType;
        })
        .attr("marker-end", function (d) {
            return "url(#" + "field" + ")";
        }).style("stroke", "#d3790f")
        .style("stroke-width", 1);

    var text = svg.append("g").selectAll("text")
        .data(force.nodes())
        .enter().append("text")
        .attr("class", function (d) {
            var classes = ""
            for (var i in d.snts) {
                classes += "text_sent_" + d.snts[i] + " "
            }
            return classes;
        })
        .attr("x", 12)
        .attr("y", 0)
        .text(function (d) {
            return d.text;
        }).on("mouseover", function (d) {
            $('.text_sent_' + d.sent).toggleClass("text_high");
        }).on("mouseout", function (d) {
            $('.text_sent_' + d.sent).toggleClass("text_high");
        })


    var linksText = svg.append("g").selectAll("text")
        .data(force.links())
        .enter().append("text")
        .attr("x", 12)
        .attr("y", 20)
        .attr("class", function (d) {

            return "link_text_" + d.sent;
        })
        .text(function (d) {
            return d.text;
        });


    function tick() {
        path.attr("d", linkArc);
        circle.attr("transform", transform);
        text.attr("transform", transform);
        //ranks.attr("transform", transform);
        linksText.attr("transform", transformLinkText);
    }

    function linkArc(d) {
        var dx = d.target.x - d.source.x,
            dy = d.target.y - d.source.y,
            dr = 0
        return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
    }

    function transformLinkText(d) {
        var dx = Math.abs(d.target.x - d.source.x),
            dy = Math.abs(d.target.y - d.source.y);

        var x = Math.min(d.target.x, d.source.x) + dx / 2
        var y = Math.min(d.target.y, d.source.y) + dy / 2

        return "translate(" + x + "," + y + ")";
    }

    function transform(d) {
        return "translate(" + d.x + "," + d.y + ")";
    }

}
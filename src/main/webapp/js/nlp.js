//Main object. Contains applications lgic and state 
var APP = {}


//number of sentances per page
 APP.step = 3;

//index of last sentance on current page
 APP.last = APP.step;

//index of first sentance on current page
 APP.first = 0;

//current sentance
 APP.sent = 0;

APP.stopwords = ["a", "as", "to", "is", "the", "it", "was", "were", "are", "15", "-RSB-", "-LSB-", "that", "of", "by", "-RRB-", "-LRB-", "and", "on"]



 //move to next sentance
 APP.stepNext = function () {
     if (APP.sent == APP.last - 1) {
         APP.next(APP.step)
     }
     highlightSent(++APP.sent)
 }

 //move to previous sentance
 APP.stepBack = function () {
     if (APP.sent == APP.first) {
          APP.next(-APP.step)
     }
     highlightSent(--APP.sent)
 }



 //merges nodes which have equal text.
 APP.distinctNodes = function () {
     var nodes = APP.nodes;
     var reduced = {}
     var text2node = {}
     APP.reduced = reduced;
     for (var i in nodes) {
         var n = nodes[i];
         if (!text2node[n.text] || APP.stopwords.indexOf(n.text) != -1) {
             reduced[n.id] = n
             text2node[n.text] = n.id
             n.ids = []
             n.snts = []
             n.ids.push(n.id)
             n.snts.push(n.sent)
         } else {
             var id = text2node[n.text]
             var n1 = reduced[id]
             n1.ids.push(n.id)
             n1.snts.push(n.sent)
         }
     }
     nodes = {}
     APP.nodes = nodes
     var nodesMap = {}
     APP.nodesMap = nodesMap;
     for (var i in reduced) {
         var r = reduced[i]
         nodes[r.id] = r
         for (var j in r.ids) {
             var id = r.ids[j]
             nodesMap[id] = r
         }
     }

 }

 //builds nodes array for current page
 APP.prepareNodes = function () {
     APP.Sent2Nodes = {}
     var s2n = APP.Sent2Nodes;
     for (var i in APP.rawNodes) {
         var n = APP.rawNodes[i]
         if (!s2n[n.sent]) {
             s2n[n.sent] = []
         }
         s2n[n.sent].push(n.id)
     }
     APP.allNodes = {}
     for (var n in APP.rawNodes) {
         var node = APP.rawNodes[n]
         APP.allNodes[node.id] = {
             id: node.id,
             text: node.text,
             pos: node.pos,
             sent: node.sent
         };
     }
 }

 //builds links array for current page
 APP.prepareLinks = function () {
     APP.links = []
     for (var n in APP.rawLinks) {
         var link = APP.rawLinks[n]
         APP.links.push({
             source: APP.nodesMap[link.source],
             target: APP.nodesMap[link.target],
             text: link.text,
             sent: link.sent
         })
     }


     APP.links = APP.links.filter(function (l) {
         if (!l.source && l.target) {
             l.target.text = l.text + " " + l.target.text
         }
         if (l.source && !l.target) {
             l.source.text += " " + l.text
         }
         return l.source && l.target
     });
 }

 APP.next = function (st, generateTable, sents) {

      APP.prepareNodes();
     var step = st;
     if (st === undefined) {
         step = APP.step
     }

     APP.nodes = {}
     APP.links = []
     APP.first += step;
     APP.last += step;
     var last = APP.last < APP.textsArray.length ? APP.last : APP.textsArray.length;
     if (!sents) {
         sents = []
         for (var i = APP.first; i <= last; i++) {
             sents.push(i)
         }
     }

     for (var i in sents) {
         var n = sents[i]
         var ids = APP.Sent2Nodes[n]
         for (var j in ids) {
             APP.nodes[ids[j]] = APP.allNodes[ids[j]]
         }
     }

      APP.distinctNodes();
      APP.prepareLinks();
      APP.refresh(generateTable);
     
 }



 APP.sendText = function (txt) {

     $.ajax({
         type: "GET",
         url: document.location.origin + "/json/graph.json",
         data: txt,
         success: function (data) {

             var json = APP.parseIfString(data);
             APP.rawNodes = json.nodes;
             APP.rawLinks = json.edges;
             APP.textsArray = json.words;
            
             APP.first = 0;
             APP.last = APP.step;
              APP.next(0, true)
             highlightSent(0);
         },
         error: function (jqXhr, textStatus, errorThrown) {
             console.log(errorThrown);
         },
         dataType: "text",
         contentType: "text/plain"

     });
 }



 APP.parseIfString = function (data) {
     if (data.constructor === String) {
         return JSON.parse(data)
     } else {
         return data;
     }
 }


 APP.refresh = function (generateTable) {
     RENDERER.render(generateTable)
 }
 
 
  
//run application on load  
$(document).ready(function(){
    APP.sendText(APP.defaultText) 
    $('#inp_text').text(APP.defaultText)
})

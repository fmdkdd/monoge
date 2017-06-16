javascript:(function(){
/*
 Bookmarklet for adding interactive functionality to PlantUML class diagrams.
 To use it, create a bookmark in your browser, and copy paste this whole script
 in the "Location" field; give it a descriptive name and save the bookmark.

 Whenever you open an SVG generated by PlantUML, visit the bookmark (through the
 Bookmark menu or the bookmark toolbar) to activate the interactive features.

 If you edit this file, make sure it can work without any newlines.  Avoid
 inline (//) comments, and do not forget your semicolons.

 Interactive features:
 - Highlight a class and its outgoing edges on mouse over
 - Click to retain the highlighting on the current class, click again to dismiss
*/

  var highlightColors = ['#FFC107', '#2196F3', '#E91E63', '#8BC34A', '#9C27B0',
                         '#00BCD4', '#3F51B5', '#FF5722', '#009688'];
  var defaultColor = '#CDDC39';
  var classToColor = {};
  var highlightStrokeWidth = 1.5;

  var svg = document.getElementsByTagName('svg')[0];
  svg.addEventListener('click', onClick);
  svg.addEventListener('mouseover', onOver);
  svg.addEventListener('mouseout', onOut);

  /* If we are inside a rect (class), highlight it and all its outgoing links */
  function onClick(ev) {
    var rect = pickRect(ev.target);
    if (rect != null && isHighlighted(rect)) {
      if (!isSticky(rect)) {
        /* Stick the highlighting */
        classToColor[rect.id] = highlightColors.shift();
        rect.classList.add('sticky');
      } else {
        /* Unstick and restore the color it held */
        rect.classList.remove('sticky');
        highlightColors.unshift(classToColor[rect.id]);
      }
    }

    ev.preventDefault();
  }

  /* If we are inside a rect (class), highlight it and all its outgoing links */
  function onOver(ev) {
    var rect = pickRect(ev.target);
    if (rect != null && !isHighlighted(rect)) {
      toggleHighlightClassAndOutgoingEdges(rect);
    }

    ev.preventDefault();
  }

  /* Unhighlight when leaving, unless it's sticky */
  function onOut(ev) {
    var rect = pickRect(ev.target);
    if (rect != null && isHighlighted(rect) && !isSticky(rect)) {
      toggleHighlightClassAndOutgoingEdges(rect);
    }
  }

  function toggleHighlightClassAndOutgoingEdges(rect) {
    toggleHighlight(rect);
    var links = findOutgoingLinksFor(rect.id);
    links.forEach(function(l) {
      toggleHighlight(l);
      findArrowHeadsFor(l).forEach(toggleHighlight);
    });
  }

  /* Unfortunately, PlantUML does not use groups for SVG elements that belong to
   * the same class.  But it looks like it outputs them in sequence.  So we can
   * lookup previous siblings until we find a rect with an id, and this will
   * give us the nearest class. */
  function pickRect(elem) {
    /* If the element doesn't have a tag name, we walked back to a comment
     * node that separates elements in the SVG.  No need going further */
    while (elem && elem.tagName) {
      if (elem.id && elem.tagName.toLowerCase() == 'rect') {
        return elem;
      }
      elem = elem.previousSibling;
    }
    return null;
  }

  /* Highlight element and save the previous stroke color as an attribute */
  function toggleHighlight(elem) {
    if (isHighlighted(elem)) {
      elem.classList.remove('highlighted');
      unsetStyle(elem, 'stroke');
      unsetStyle(elem, 'strokeWidth');
      /* Reset (some) arrow heads fill */
      if (elem.tagName.toLowerCase() == 'polygon' && elem.getAttribute('fill') == '#333333') {
        unsetStyle(elem, 'fill');
      }
    } else {
      elem.classList.add('highlighted');
      var color = highlightColors[0] || defaultColor;
      setStyle(elem, 'stroke', color);
      setStyle(elem, 'strokeWidth', highlightStrokeWidth);
      /* We want to fill arrow heads, but skip those that are hollow.  The
       * stroke color should match the fill color, but one is in rgb() format
       * and the other is in #rrggbb, so cannot easily compare.  Hence, the
       * color is hardcoded.
       */
      if (elem.tagName.toLowerCase() == 'polygon' && elem.getAttribute('fill') == '#333333') {
        setStyle(elem, 'fill', color);
      }
    }
  }

  function isHighlighted(elem) {
    return elem.classList.contains('highlighted');
  }

  function isSticky(elem) {
    return elem.classList.contains('sticky');
  }

  function setStyle(elem, style, value) {
    elem.setAttribute('data-prev-' + style, elem.style[style]);
    elem.style[style] = value;
  }

  function unsetStyle(elem, style) {
    elem.style[style] = elem.getAttribute('data-prev-' + style);
    elem.removeAttribute('data-prev-' + style);
  }

  /* PlantUML sets id="A-B" on path that represent links from A to B */
  function findOutgoingLinksFor(id) {
    return svg.querySelectorAll('path[id^=' + id + '-]');
  }

  function findIncomingLinksFor(id) {
    return svg.querySelectorAll('path[id$=-' + id + ']');
  }

  /* If we want the arrow heads, they are all the polygons following the
   * paths */
  function findArrowHeadsFor(link) {
    var heads = [];
    link = link.nextSibling;
    while (link && link.tagName && link.tagName.toLowerCase() == 'polygon') {
      heads.push(link);
      link = link.nextSibling;
    }
    return heads;
  }

}());

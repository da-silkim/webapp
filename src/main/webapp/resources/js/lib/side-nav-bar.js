/*!
* Based on articles on
* https://gomakethings.com
*/

var sideNavBar = function () {
  /**
  * Element.closest() polyfill
  * https://developer.mozilla.org/en-US/docs/Web/API/Element/closest#Polyfill
  */
  if (!Element.prototype.closest) {
    if (!Element.prototype.matches) {
      Element.prototype.matches = Element.prototype.msMatchesSelector || Element.prototype.webkitMatchesSelector;
    }
    Element.prototype.closest = function (s) {
      var el = this;
      var ancestor = this;
      if (!document.documentElement.contains(el)) return null;
      do {
        if (ancestor.matches(s)) return ancestor;
        ancestor = ancestor.parentElement;
      } while (ancestor !== null);
      return null;
    };
  }

  // Trap Focus 
  // https://hiddedevries.nl/en/blog/2017-01-29-using-javascript-to-trap-focus-in-an-element
  //
  function trapFocus(element) {
    var focusableEls = element.querySelectorAll('a[href]:not([disabled]), button:not([disabled]), textarea:not([disabled]), input[type="text"]:not([disabled]), input[type="radio"]:not([disabled]), input[type="checkbox"]:not([disabled]), select:not([disabled])');
    var firstFocusableEl = focusableEls[0];
    var lastFocusableEl = focusableEls[focusableEls.length - 1];
    var KEYCODE_TAB = 9;

    element.addEventListener('keydown', function (e) {
      var isTabPressed = (e.key === 'Tab' || e.keyCode === KEYCODE_TAB);

      if (!isTabPressed) {
        return;
      }

      if (e.shiftKey) /* shift + tab */ {
        if (document.activeElement === firstFocusableEl) {
          lastFocusableEl.focus();
          e.preventDefault();
        }
      } else /* tab */ {
        if (document.activeElement === lastFocusableEl) {
          firstFocusableEl.focus();
          e.preventDefault();
        }
      }
    });
  }

  //
  // Settings
  //
  var settings = {
    speedOpen: 50,
    speedClose: 350,
    activeClass: 'is-active',
    visibleClass: 'is-visible',
    selectorTarget: '[data-side-nav-bar-target]',
    selectorTrigger: '[data-side-nav-bar-trigger]',
    selectorClose: '[data-side-nav-bar-close]',
  };

  //
  // Methods
  //

  // Toggle accessibility
  var toggleAccessibility = function (e) {
    if( $(e).attr("aria-expanded") == 'true' ){
      $(e).attr('aria-expanded', false);
    }
    else {
      $(e).attr('aria-expanded', true);
    }
  };

  // Open sideNavBar
  var openSideNavBar = function (trigger) {
    // Find target
    var target = document.getElementById(trigger.getAttribute('aria-controls'));

    // Make it active
    target.classList.add(settings.activeClass);

    // Make body overflow hidden so it's not scrollable
    document.documentElement.style.overflow = 'hidden';
    // document.querySelector("#root").style.filter = "blur(1px)";

    // Toggle accessibility
    toggleAccessibility(trigger);

    // Make it visible
    setTimeout(function () {
      target.classList.add(settings.visibleClass);
      trapFocus(target);
    }, settings.speedOpen);
  };

  // Close sideNavBar
  var closeSideNavBar = function (e) {
    // Find target
    var closestParent = e.closest(settings.selectorTarget),
      childrenTrigger = document.querySelector('[aria-controls="' + closestParent.id + '"');

    // Make it not visible
    closestParent.classList.remove(settings.visibleClass);

    // Remove body overflow hidden
    document.documentElement.style.overflow = '';
    // document.querySelector("#root").style.filter = "";

    // Toggle accessibility
    toggleAccessibility(childrenTrigger);

    // Make it not active
    setTimeout(function () {
      closestParent.classList.remove(settings.activeClass);

      if( $(e).closest(".itemModal").length > 0 ){
        $(e).closest(".itemModal").remove()
      }

    }, settings.speedClose);
  };

  // Click Handler
  var clickHandler = function (e) {
    // Find elements
    var toggle = e.target,
      open = toggle.closest(settings.selectorTrigger),
      close = toggle.closest(settings.selectorClose);

    // Open sideNavBar when the open button is clicked
    if (open) {
      openSideNavBar(open);
    }

    // Close sideNavBar when the close button (or overlay area) is clicked
    if (close) {
      closeSideNavBar(close);
    }

    // Prevent default link behavior
    if (open || close) {
      e.preventDefault();
    }
  };

  // Keydown Handler, handle Escape button
  var keydownHandler = function (e) {
    if (e.key === 'Escape' || e.keyCode === 27) {
      // Find all possible sideNavBars
      var sideNavBars = document.querySelectorAll(settings.selectorTarget),
        i;

      // Find active sideNavBars and close them when escape is clicked
      for (i = 0; i < sideNavBars.length; ++i) {
        if (sideNavBars[i].classList.contains(settings.activeClass)) {
          closeSideNavBar(sideNavBars[i]);
        }
      }
    }
  };

  //
  // Inits & Event Listeners
  //
  document.addEventListener('click', clickHandler, false);
  document.addEventListener('keydown', keydownHandler, false);
};

sideNavBar();

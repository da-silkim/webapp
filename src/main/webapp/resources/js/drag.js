
  /* drag */
  var swiper = new Swiper(".mySwiper3", {
        pagination: {
          el: ".swiper-pagination",
          type: "fraction",
        },
        slidesPerView: '1.15',
        loop: true,
        spaceBetween: 10,
      });


      var appendNumber = 4;
      var prependNumber = 1;
      document
        .querySelector(".prepend-2-slides")
        .addEventListener("click", function (e) {
          e.preventDefault();
          swiper.prependSlide([
            '<div class="swiper-slide">Slide ' + --prependNumber + "</div>",
            '<div class="swiper-slide">Slide ' + --prependNumber + "</div>",
          ]);
        });
      document
        .querySelector(".prepend-slide")
        .addEventListener("click", function (e) {
          e.preventDefault();
          swiper.prependSlide(
            '<div class="swiper-slide">Slide ' + --prependNumber + "</div>"
          );
        });
      document
        .querySelector(".append-slide")
        .addEventListener("click", function (e) {
          e.preventDefault();
          swiper.appendSlide(
            '<div class="swiper-slide">Slide ' + ++appendNumber + "</div>"
          );
        });
      document
        .querySelector(".append-2-slides")
        .addEventListener("click", function (e) {
          e.preventDefault();
          swiper.appendSlide([
            '<div class="swiper-slide">Slide ' + ++appendNumber + "</div>",
            '<div class="swiper-slide">Slide ' + ++appendNumber + "</div>",
          ]);
        });

    const slider = document.querySelector('.slider');
    const sliderInner = slider.querySelector('.slider-inner');
    const slides = slider.querySelectorAll('.slide');
    const prevBtn = slider.querySelector('.slider-prev');
    const nextBtn = slider.querySelector('.slider-next');
    let slideWidth = slides[0].offsetWidth;
    let slideIndex = 0;

    function slideTo(index) {
      sliderInner.style.transform = `translateX(-${slideWidth * index}px)`;
      currentSlideIndex = index;
    }

    function slideTo(index) {
        sliderInner.style.transform = `translateX(-${slideWidth * index}px)`;
        slideIndex = index;
    }

    function prevSlide() {
        slideIndex--;
        if (slideIndex < 0) {
            slideIndex = slides.length - 1;
        }
        slideTo(slideIndex);
    }

    function nextSlide() {
        slideIndex++;
        if (slideIndex >= slides.length) {
            slideIndex = 0;
        }
        slideTo(slideIndex);
    }

    prevBtn.addEventListener('click', prevSlide);
    nextBtn.addEventListener('click', nextSlide);
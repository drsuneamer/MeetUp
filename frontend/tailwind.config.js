/** @type {import('tailwindcss').Config} */
// const colors = require('tailwindcss/colors');
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  theme: {
    colors: {
      title: '#0552AC', // active
      hover: '#033774',
      body: '#191F28',
      label: '#737475',
      placeholder: '#BCBFC1',
      line: '#E5E8EB',
      background: '#FFFFFF',
      offWhite: '#F9FAFB',
      point: '#6C91F4',
      primary: '#26C196',
      footer: '#AEC2FA',
      cancel: '#B70038', // error
      cancelhover: '#B70038',
      disabled: '#AEC2FA', // disabled
      text: {
        DEFAULT: '#191F28',
        label: '#737475',
      },
    },
    fontFamily: {
      pre: ['Pretendard Variable'],
      damion: ['Damion'], // meetup logo
      dots: ['Raleway Dots'], // version
      script: ['Nanum Pen Script'],
    },
    fontSize: {
      xs: '15px', // meetup title
      s: '18px', // modal input
      m: '20px', // modal title
      l: '25px', // calendar title(month/date/day)
      xl: '30px', // sidebar title
      '2xl': '100px', // login title
    },
    screens: {
      // responsive breakpoint
      sm: '640px',
      md: '768px',
      lg: '1024px', // default
      xl: '1280px',
      '2xl': '1536px',
    },
    extend: {
      width: {
        xs: '320px', // modal small
        s: '400px', // login
        m: '650px', // modal medium
        l: '1000px', // team setting page
      },
      height: {
        xs: '3px', // input underline
        s: '50px', // button small
        l: '60px', // button large
      },
      spacing: {},
      dropShadow: {
        shadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
        button: '0px 2.5px 2.5px rgba(0, 0, 0, 0.2)',
      },
      boxShadow: {
        shadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
        button: '0px 2.5px 2.5px rgba(0, 0, 0, 0.2)',
      },
      borderRadius: {
        DEFAULT: '8px',
        login: '15px',
      },
      animation: {
        'bounce-late': 'bounce 2s linear infinite',
      },
    },
  },
  plugins: [require('tailwind-scrollbar-hide'),
require('tailwind-scrollbar')({ nocompatible: true }),],
  variants: {
    scrollbar: ['rounded']
},
  important: true,
};

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
      cancel: '#B70038', // error
      cancelhover: '#B70038',
      text: {
        DEFAULT: '#191F28',
        label: '#737475',
      },
    },
    fontFamily: {
      damion: ['Damion', 'cursive'], // meetup logo
      noto: ['Noto Sans KR', 'sans-serif'], // body
    },
    fontSize: {
      xs: '15px', // meetup title
      s: '18px', // modal input
      m: '20px', // modal title
      l: '25px', // calendar title(month/date/day)
      xl: '30px', // sidebar title
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
      },
      borderRadius: {
        DEFAULT: '8px',
        login: '20px',
      },
    },
  },
  plugins: [],
  important: true,
};
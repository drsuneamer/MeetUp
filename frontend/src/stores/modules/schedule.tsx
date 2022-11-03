import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
  
// const initialState = {
//     loading: false,
//     title: '',
//     time: '',
//     content: '',
//     team: '',
//   };

// // export const registerSchedule = 
// // const registerSchedule = 
// // const fetchSchedule = 

// export const scheduleSlice = createSlice({
//   name: 'schedule',
//   initialState,
//   reducers: {},
//   extraReducers: {
//       // POST
//       [registerSchedule.pending]: (state) => {
//         state.loading = false;
//         console.log('register pending');
//       },
//       [registerSchedule.fulfilled]: (state, action) => {
//         state.loading = true;
//         console.log(action.payload); // dogId
//         console.log('register fulfilled');
//       },
//       [registerSchedule.rejected]: (state) => {
//         state.loading = false;
//         console.log('register rejected');
//       },
//       //  GET
//       [fetchSchedule.pending]: (state) => {
//         state.loading = false;
//         console.log('fetching pending');
//       },
//       [fetchSchedule.fulfilled]: (state, action) => {
//         state.loading = true;
//         console.log('fetching fulfilled');
//       },
//       [fetchSchedule.rejected]: (state) => {
//         state.loading = false;
//         console.log('fetching rejected');
//       },
//   }
// });

// export const  scheduleSelector = (state) => 
// export default scheduleSlice.reducer;

import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tMember } from '../../types/members';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';
import { useSelector } from 'react-redux';
import axios from 'axios';

type memberInitialState = {
    loading: boolean;
    members: Array<tMember>;
};

const initialState: memberInitialState = {
    loading: false,
    members: [{id: '', nickname: ''}],
};

export const fetchMemberList = createAsyncThunk('members/fetchMemberList', async () => {
    console.log('hello')
    // const meetUpId = useSelector((state: any) => state.channels.value).id;
    try {
        const res = await axiosInstance.get(`/meetup/users/1`).then((res) => {
            console.log(res)
            return res
        })
        return res
    } 
    catch (err) {
        console.log('디스팟치')
      }
});

// export const fetchMemberList = createAsyncThunk('members/fetchMemberList', async (thunkAPI) => {
//     try {
//       const meetUpId = useSelector((state: any) => state.channel.value).id;
//       const res = await axios({
//         url: `/api/meetup/users/${meetUpId}`,
//         method: 'get',
//         headers: {
//           Authorization: `Bearer `,
//         },
//       }).then((res) => {
//         console.log('된다 ', res);
//       });
//       return res;
//     } catch (err) {
//       console.log('안된다')
//       return thunkAPI;
//     }
//   });

  
const memberSlice = createSlice({
    name: 'member',
    initialState,
    reducers: {},
    extraReducers: {
        [fetchMemberList.pending.toString()]: (state) => {
            state.loading = false;
          },
        [fetchMemberList.fulfilled.toString()]: (state, action) => {
        state.loading = true;
        state.members = action.payload;
        console.log('fulfilled')
        },
        [fetchMemberList.rejected.toString()]: (state) => {
        state.loading = false;
        console.log('rejected');
        },  
      },   
    });

const { reducer } = memberSlice;
export const memberSelector = (state: RootState) => state.members;
export default reducer;
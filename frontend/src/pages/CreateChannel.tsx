import { useState } from 'react';

import Layout from '../components/layout/Layout';
import MultipleLevelSelection from '../components/MultipleLevelSelection';

interface Category {
  categoryId: string;
  parentId: string;
  name: string;
}

function CreateChannel() {
  const [category, setCategory] = useState<Category>();
  console.log(category); // 보내줘야 할 값 (저장하기 클릭 시)

  const lv1Categories = [
    //팀
    { categoryId: '1', name: '1반', parentId: '0' },
    { categoryId: '2', name: '2반', parentId: '0' },
    { categoryId: '3', name: '3반', parentId: '0' },
    { categoryId: '4', name: '4반', parentId: '0' },
    { categoryId: '9', name: '9반', parentId: '0' },
  ];

  const lv2Categories = [
    //채널
    { categoryId: '1', name: '101', parentId: '1' },
    { categoryId: '2', name: '201', parentId: '1' },
    { categoryId: '1', name: '새로운 채널 생성하기', parentId: '1' },
    { categoryId: '3', name: '202', parentId: '2' },
    { categoryId: '4', name: '203', parentId: '2' },
    { categoryId: '5', name: '204', parentId: '2' },
    { categoryId: '6', name: '205', parentId: '2' },
    { categoryId: '7', name: '206', parentId: '2' },
    { categoryId: '8', name: '207', parentId: '2' },
    { categoryId: '9', name: '208', parentId: '2' },
  ];

  const categories: Category[] = [...lv1Categories, ...lv2Categories];
  const categoriesByParentId = (parentId: string | number) => categories.filter((category) => category.parentId === `${parentId}`);
  return (
    <Layout>
      <div className="text-m mx-[15vw] pt-[20vh]">
        <div className="mb-10 z-50">
          <div className="font-bold text-title">알림을 받을 채널 선택하기</div>
          <div className="">
            <MultipleLevelSelection
              initialItems={categoriesByParentId(0)}
              getItemKey={(item) => item.categoryId}
              getItemLabel={(item) => item.name}
              getNestedItems={(item) => categoriesByParentId(item.categoryId)}
              hasNestedItems={(_, level) => level < 2}
              isEqual={(item, item2) => item?.categoryId === item2?.categoryId}
              placeholder="내 MM의 모든 팀/채널 확인하기"
              onChange={setCategory}
            />
          </div>
        </div>

        <div className="mb-5">
          <div className="font-bold text-title">MM 채널 이름 (변경 가능)</div>
          <div className="flex justify-center">
            <input
              // onChange={}
              type="text"
              placeholder="ex. 서울_1반_A102"
              className="w-full text-center placeholder-placeholder border-b-2 border-b-title py-1 px-2 mb-10 placeholder:text-s focus:outline-none focus:border-b-footer"
            />
          </div>
        </div>

        <div className="font-bold text-title">
          <div>달력에 표시할 색상 선택</div>
          <div> {/* color picker */}</div>
        </div>

        <button className="-z-50 bg-title rounded drop-shadow-shadow text-background font-medium w-full h-s my-2 hover:bg-hover">저장하기</button>
      </div>
    </Layout>
  );
}

export default CreateChannel;

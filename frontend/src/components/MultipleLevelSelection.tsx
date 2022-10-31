import React, { memo, Key } from 'react';

import { useSelect, useClassNames, UseSelectProps, ClassName } from '../utils/SetMultipleDropdown';
import './selection_styles.css';

export function MultipleLevelSelectionComponent<TItem = string>({
  classes,
  getItemKey,
  getItemLabel,
  hasNestedItems,
  ...rest
}: MultipleLevelSelectionProps<TItem>) {
  const { open, label, renderingItems, toggle, handleClickItem, isSelectedItem } = useSelect({
    ...rest,
    getItemLabel,
    hasNestedItems,
  });

  const classesNames = useClassNames(classes);

  return (
    <div className={'flex bg-title rounded items-center justify-center drop-shadow-shadow text-s font-medium w-full h-s my-2 cursor-pointer'}>
      <div onClick={toggle} className="text-background">
        <span>{label}</span>
      </div>
      <svg onClick={toggle} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="white" className="absolute right-0 mr-3 w-6 h-6">
        <path
          fillRule="evenodd"
          d="M12.53 16.28a.75.75 0 01-1.06 0l-7.5-7.5a.75.75 0 011.06-1.06L12 14.69l6.97-6.97a.75.75 0 111.06 1.06l-7.5 7.5z"
          clipRule="evenodd"
        />
      </svg>
      {renderingItems && (
        <div className={classesNames.selectionEntries(open)}>
          <div className="flex flex-row">
            {Object.keys(renderingItems).map((level: string) => (
              <ul key={`entry-level-${level}`} className={classesNames.levelEntry()}>
                {renderingItems[+level].map((item) => {
                  const selected = isSelectedItem(item, +level);
                  const nestable = hasNestedItems(item, +level);
                  return (
                    <li
                      key={getItemKey(item)}
                      className={classesNames.levelItem({
                        nestable,
                        selected,
                      })}
                      title={getItemLabel(item)}
                      onClick={handleClickItem(item, +level)}
                    >
                      {getItemLabel(item)}
                    </li>
                  );
                })}
              </ul>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

const MultipleLevelSelection = memo(MultipleLevelSelectionComponent) as typeof MultipleLevelSelectionComponent & React.ComponentType<any>;
MultipleLevelSelection.displayName = 'MultipleLevelSelection';

export default MultipleLevelSelection;

export interface MultipleLevelSelectionProps<TItem> extends UseSelectProps<TItem> {
  getItemKey: (item: TItem) => Key; // React key generator based on item
  classes?: Partial<Record<ClassName, string>>; // Custom classe names
}

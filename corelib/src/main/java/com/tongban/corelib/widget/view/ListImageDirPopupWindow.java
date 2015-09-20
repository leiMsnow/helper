package com.tongban.corelib.widget.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongban.corelib.R;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.model.ImageFolder;

import java.util.List;

/**
 * 图片文件夹
 */
public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFolder> {

    private ListView mListDir;
    private OnImageDirSelected mImageDirSelected;

    private String selectDir = "";

    public void setSelectDir(String selectDir) {
        this.selectDir = selectDir;
    }

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFolder> data, View convertView) {
        super(convertView, width, height, true, data);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new QuickAdapter<ImageFolder>(mContext,
                R.layout.item_list_dir_item, mData) {
            @Override
            public void convert(BaseAdapterHelper helper, ImageFolder item) {

                if (selectDir.equals(item.getName())) {
                    helper.setVisible(R.id.iv_selected, View.VISIBLE);
                } else {
                    helper.setVisible(R.id.iv_selected, View.GONE);
                }

                helper.setText(R.id.id_dir_item_name, item.getName());
                helper.setImageBitmap(R.id.id_dir_item_image,
                        item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        });
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (mImageDirSelected != null) {
                    mImageDirSelected.selected(mData.get(position));
                }
            }
        });
    }

    @Override
    public void init() {

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {

    }

    public interface OnImageDirSelected {
        void selected(ImageFolder folder);
    }

}

package com.tongban.im.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.utils.LocationUtils;
import com.tongban.im.widget.view.AlertView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener {
    private Button btnSubmit;
    private EditText etGroupName;
    private ImageView ivSetGroupIcon;
    private EditText etMap;
    private TextView tvGroupLabel;

    private AlertView dialog;
    private LinearLayout mCamera;
    private LinearLayout mGallery;

    private int mGroupType;
    private String titleName;

    /**
     * 经纬度
     */
    private double longitude, latitude;
    /**
     * 位置信息
     */
    private String province, city, county, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(titleName.equals(getString(R.string.create_group)) ? getString(R.string.create_group) :
                getString(R.string.create) + titleName);

    }

    @Override
    protected int getLayoutRes() {
        if (getIntent().getExtras() != null) {
            mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
            titleName = getIntent().getExtras().getString(Consts.KEY_GROUP_TYPE_NAME, "");
        }

        setToolbarTheme(mGroupType);
        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {
        ivSetGroupIcon = (ImageView) findViewById(R.id.iv_group_icon);
        btnSubmit = (Button) findViewById(R.id.btn_create);
        etGroupName = (EditText) findViewById(R.id.et_group_name);
        etMap = (EditText) findViewById(R.id.et_group_location);
        tvGroupLabel = (TextView) findViewById(R.id.tv_group_label);

    }

    @Override
    protected void initListener() {
        ivSetGroupIcon.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etMap.setOnClickListener(this);
        tvGroupLabel.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == ivSetGroupIcon) {
            createDialog();
        } else if (v == btnSubmit) {
            String groupName = etGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)) {
                ToastUtil.getInstance(mContext).showToast("请输入圈子名称");
                return;
            }
            if (longitude == 0.0)
                longitude = (Double) SPUtils.get(mContext, Consts.LONGITUDE, 0.0);
            if (latitude == 0.0)
                latitude = (Double) SPUtils.get(mContext, Consts.LATITUDE, 0.0);
            if (address == null || "".equals(address))
                address = (String) SPUtils.get(mContext, Consts.ADDRESS, "");
            if (county == null || "".equals(county))
                county = (String) SPUtils.get(mContext, Consts.COUNTY, "");
            if (city == null || "".equals(city))
                city = (String) SPUtils.get(mContext, Consts.CITY, "");
            if (province == null || "".equals(province))
                province = (String) SPUtils.get(mContext, Consts.PROVINCE, "");
            GroupApi.getInstance().createGroup(groupName, mGroupType, longitude, latitude, address,
                    null, null, null, province, city, county, this);
        } else if (v == etMap) {
            Intent intent = new Intent(mContext, PoiSearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == tvGroupLabel) {
            startActivity(new Intent(mContext, LabelListActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == CameraUtils.OPEN_CAMERA) {
            File file = CameraUtils.getImageFile();
            if (file.exists()) {
                if (file.length() > 100) {
                    String newFile = CameraUtils.saveToSD(file
                            .getAbsolutePath());
                    Intent intent = new Intent(mContext, ClipImageBorderViewActivity.class);
                    intent.putExtra("newFile", newFile);
                    startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            Intent intent = new Intent(mContext, ClipImageBorderViewActivity.class);
            intent.putExtra("newFile", newFile);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
        } else if (requestCode == CameraUtils.PHOTO_REQUEST_CUT) {
            if (resultCode == RESULT_OK) {
                byte[] b = data.getByteArrayExtra("bitmap");
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                ivSetGroupIcon.setImageBitmap(bitmap);
            }
        }
    }

    // 打开相机的提示框
    private void createDialog() {
        if (dialog == null) {
            dialog = new AlertView(this);
            mCamera = (LinearLayout) dialog.findViewById(R.id.camera);
            mGallery = (LinearLayout) dialog.findViewById(R.id.gallery);

            mCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Utils.takePhoto(mContext, null);
                    CameraUtils.takePhoto(mContext);
                    dialog.cancel();
                }
            });
            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Utils.openPhotoAlbum(mContext, null);
                    CameraUtils.openPhotoAlbum(mContext);
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }

    private Bitmap getLocalBitmap(String url) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onEventMainThread(Object obj) {
        if (obj instanceof Group) {
            // 创建成功
            ToastUtil.getInstance(mContext).showToast("创建成功");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.topic_detail) {
            startActivity(new Intent(mContext, TopicActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

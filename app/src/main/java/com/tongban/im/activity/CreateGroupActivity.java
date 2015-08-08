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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    public static int SELECT_LOCATION = 310;
    public static int SELECT_LABEL = 320;


    private ImageView ivSetGroupIcon;
    private EditText etGroupName, etDesc;
    private TextView tvGroupLabel, tvLocation, tvAge, tvSchool, tvLife;
    private CheckBox chbSecret, chbAgree;
    private Button btnSubmit;

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
        LocationUtils.get(mContext).start();
        setTitle(titleName.equals(getString(R.string.create_group)) ? getString(R.string.create_group) :
                getString(R.string.create) + titleName);

    }

    @Override
    protected int getLayoutRes() {
        if (getIntent().getExtras() != null) {
            mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
            titleName = getIntent().getExtras().getString(Consts.KEY_GROUP_TYPE_NAME, "");
            setToolbarTheme(mGroupType);
        }

        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {

        ivSetGroupIcon = (ImageView) findViewById(R.id.iv_group_icon);
        etGroupName = (EditText) findViewById(R.id.et_group_name);
        etGroupName.requestFocus();
        etDesc = (EditText) findViewById(R.id.et_group_desc);

        tvGroupLabel = (TextView) findViewById(R.id.tv_group_label);
        tvLocation = (TextView) findViewById(R.id.tv_group_location);

        tvAge = (TextView) findViewById(R.id.tv_age);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvLife = (TextView) findViewById(R.id.tv_life);

        chbSecret = (CheckBox) findViewById(R.id.chb_secret);
        chbAgree = (CheckBox) findViewById(R.id.chb_agreement);

        btnSubmit = (Button) findViewById(R.id.btn_create);

        setTextVisible();
    }

    //设置文本的可见度
    private void setTextVisible() {
        switch (mGroupType) {
            case GroupType.AGE:
                tvAge.setVisibility(View.VISIBLE);
                break;
            case GroupType.LIFE:
                tvLife.setVisibility(View.VISIBLE);
                break;
            case GroupType.CLASSMATE:
                tvSchool.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void initListener() {
        ivSetGroupIcon.setOnClickListener(this);

        tvLocation.setOnClickListener(this);
        tvGroupLabel.setOnClickListener(this);
        tvAge.setOnClickListener(this);
        tvLife.setOnClickListener(this);
        tvSchool.setOnClickListener(this);

        chbAgree.setOnCheckedChangeListener(this);
        chbSecret.setOnCheckedChangeListener(this);

        btnSubmit.setOnClickListener(this);
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
            GroupApi.getInstance().createGroup(groupName, mGroupType, longitude, latitude, address,
                    null, null, null, province, city, county, this);
        } else if (v == tvLocation) {
            Intent intent = new Intent(mContext, PoiSearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            bundle.putString(Consts.KEY_SELECTED_POI_NAME, address);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_LOCATION);
        } else if (v == tvGroupLabel) {
            Intent intent = new Intent(mContext, LabelListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_LABEL);
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
        } else if (requestCode == SELECT_LOCATION) {
            address = data.getStringExtra(Consts.KEY_SELECTED_POI_NAME);
            longitude = data.getDoubleExtra(Consts.LONGITUDE, 0.0);
            latitude = data.getDoubleExtra(Consts.LATITUDE, 0.0);
            tvLocation.setText(address);
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
                    CameraUtils.takePhoto(mContext);
                    dialog.cancel();
                }
            });
            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.openPhotoAlbum(mContext);
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }

    public void onEventMainThread(Object obj) {
        if (obj instanceof Group) {
            // 创建成功
            ToastUtil.getInstance(mContext).showToast("创建成功");
            finish();
        } else if (obj instanceof BDLocation) {
            longitude = (Double) SPUtils.get(mContext, Consts.LONGITUDE, 0.0);
            latitude = (Double) SPUtils.get(mContext, Consts.LATITUDE, 0.0);
            address = (String) SPUtils.get(mContext, Consts.ADDRESS, "");
            county = (String) SPUtils.get(mContext, Consts.COUNTY, "");
            city = (String) SPUtils.get(mContext, Consts.CITY, "");
            province = (String) SPUtils.get(mContext, Consts.PROVINCE, "");
            tvLocation.setText(address);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chbSecret) {

        } else if (buttonView == chbAgree) {

        }
    }
}

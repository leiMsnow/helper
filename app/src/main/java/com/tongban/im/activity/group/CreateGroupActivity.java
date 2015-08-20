package com.tongban.im.activity.group;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.ClipImageBorderViewActivity;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.CameraView;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener {

    public static int SELECT_LOCATION = 310;
    public static int SELECT_LABEL = 320;


    private ImageView ivSetGroupIcon;
    private EditText etGroupName, etDesc;
    private TextView tvGroupLabel, tvLocation, tvBirthday, tvLife;
    private CheckBox chbSecret, chbAgree;
    private Button btnSubmit;

    private CameraView mCameraView;

    private int mGroupType;
    private String titleName;

    private DatePickerDialog mDatePickerDialog;
    private byte[] mGroupIcon;
    /**
     * 经纬度
     */
    private double longitude, latitude;
    /**
     * 位置信息
     */
    private String province, city, county, address, birthday, tags, declaration;

    private ImageUrl groupAvatar = new ImageUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(titleName.equals("") ? getString(R.string.create_group) :
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

        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
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
                tvBirthday.setVisibility(View.VISIBLE);
                break;
            case GroupType.LIFE:
                tvLife.setVisibility(View.VISIBLE);
                break;
            case GroupType.CLASSMATE:
                tvLocation.setHint(R.string.create_school);
                break;
        }
    }

    @Override
    protected void initListener() {
        ivSetGroupIcon.setOnClickListener(this);

        tvLocation.setOnClickListener(this);
        tvGroupLabel.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvLife.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        longitude = (Double) SPUtils.get(mContext, Consts.LONGITUDE, -1.0D);
        latitude = (Double) SPUtils.get(mContext, Consts.LATITUDE, -1.0D);
        province = (String) SPUtils.get(mContext, Consts.PROVINCE, "");
        city = (String) SPUtils.get(mContext, Consts.CITY, "");
        county = (String) SPUtils.get(mContext, Consts.COUNTY, "");
        address = (String) SPUtils.get(mContext, Consts.ADDRESS, "");
        setLocationInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == ivSetGroupIcon) {
            createDialog();
        } else if (v == btnSubmit) {
            final String groupName = etGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)) {
                ToastUtil.getInstance(mContext).showToast("请输入圈子名称");
                return;
            }
            if (!chbAgree.isChecked()) {
                ToastUtil.getInstance(mContext).showToast("请勾选'" + getString(R.string.group_agreement) + "'");
                return;
            }
            FileUploadApi.getInstance().uploadFile(mGroupIcon, null, new UploadFileCallback() {

                @Override
                public void uploadSuccess(String minUrl, String midUrl, String maxUrl) {
                    groupAvatar.setMin(minUrl);
                    groupAvatar.setMid(midUrl);
                    groupAvatar.setMax(maxUrl);

                    declaration = etDesc.getText().toString().trim();

                    GroupApi.getInstance().createGroup(groupName, mGroupType, longitude, latitude, address,
                            birthday, tags, declaration, groupAvatar, chbSecret.isChecked(),
                            CreateGroupActivity.this);
                }
            });


        } else if (v == tvLocation) {
            Intent intent = new Intent(mContext, SearchPoiActivity.class);
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
        } else if (v == tvBirthday) {
            openDatePicker();
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
            mGroupIcon = data.getByteArrayExtra("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(mGroupIcon, 0, mGroupIcon.length);
            ivSetGroupIcon.setImageBitmap(bitmap);
        } else if (requestCode == SELECT_LOCATION) {
            longitude = data.getDoubleExtra(Consts.LONGITUDE, -1.0D);
            latitude = data.getDoubleExtra(Consts.LATITUDE, -1.0D);
            address = data.getStringExtra(Consts.KEY_SELECTED_POI_NAME);
            tvLocation.setText(address);

            setLocationInfo();
        }
    }

    // 打开相机的提示框
    private void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(this);
        }
        mCameraView.show();
    }

    //打开时间选择器
    private void openDatePicker() {
        if (mDatePickerDialog == null) {
            Calendar c = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);

                    if (month.length() == 1) month = "0" + month;
                    if (day.length() == 1) day = "0" + day;

                    birthday = year + "-" + month + "-" + day;
                    tvBirthday.setText(birthday);
                }
            }, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.show();
    }

    //圈子创建成功Event
    public void onEventMainThread(BaseEvent.CreateGroupEvent obj) {
        // 创建成功
        ToastUtil.getInstance(mContext).showToast("创建成功");
        finish();
    }

    //标签选择完成Event
    public void onEventMainThread(BaseEvent.LabelEvent obj) {
        tags = obj.getLabel();
        tvGroupLabel.setText(tags);
    }


    //拼接当前位置，用于接口接收[省，市，区，详细地址]
    private void setLocationInfo() {
        address = province + "," + city + "," + county + "," + address;
    }
}

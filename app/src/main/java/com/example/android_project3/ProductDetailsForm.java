package com.example.android_project3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductDetailsForm extends Fragment {

    private View view;
    private String barcode;
    private ImageView imageView;
    private Uri imageUri;
    ProgressDialog progressDialog;
    private StorageReference storageReference;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private ArrayList<String> imagesUrls = new ArrayList<String>();
    private int uploads = 0;
    private Button submitBtn;
    //    private Button uploadImgBtn;
//    private Button addImgBtn;
    private TextView barcodeInput;
    private boolean areImagesLimited = true;

    private EditText title;
    private EditText description;
    private EditText review;
    private EditText recyclabilityDetails;
    private EditText energyEfficiencyDetails;
    private EditText packgingDetails;
    private RatingBar productRatings;
    private RadioGroup radioGroup;
    private RadioButton radioBtnYes, radioBtnNo;
    private RatingBar sustainabilityRatings;
    private String _code = null, _title = null, _description = null, _review = null, _recyclabilityDetails = null, _energyEfficiencyDetails = null, _packgingDetails = null;
    private float _ratings, _sustainabilityRatings;
    private String productRecyclable = "";


    public ProductDetailsForm() {
        // Required empty public constructor
    }


    public static ProductDetailsForm newInstance(String barcode) {
        ProductDetailsForm fragment = new ProductDetailsForm();
        Bundle args = new Bundle();
        args.putString("barcode", barcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            barcode = getArguments().getString("barcode", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_details_form, container, false);

        TextView barcodeView = view.findViewById(R.id.barcodeInput);
        String b = getArguments().getString("barcode","");
        barcodeView.setText(b);

//        addImgBtn = view.findViewById(R.id.addImgBtn);
//        uploadImgBtn = view.findViewById(R.id.uploadImgBtn);
        submitBtn = view.findViewById(R.id.submitBtn);

        barcodeInput = view.findViewById(R.id.barcodeInput);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        review = view.findViewById(R.id.review);
        recyclabilityDetails = view.findViewById(R.id.recyclablityDetails);
        energyEfficiencyDetails = view.findViewById(R.id.energyEfficiencyDetails);
        packgingDetails = view.findViewById(R.id.packgingDetails);

        radioBtnYes = view.findViewById(R.id.radioButtonRecyclableYes);
        radioBtnNo = view.findViewById(R.id.radioButtonRecyclableNo);
        radioGroup = view.findViewById(R.id.radioGroupRecyclable);
        productRatings = view.findViewById(R.id.productRatings);
        sustainabilityRatings = view.findViewById(R.id.sustainabilityRatings);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioButtonRecyclableYes) {
                    productRecyclable = "YES";
                } else if (checkedId == R.id.radioButtonRecyclableNo) {
                    productRecyclable = "NO";
                }

                Log.e("productRecyclable::::", productRecyclable);
            }
        });

        productRatings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Handle the rating change
                Toast.makeText(requireContext(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                _ratings = rating;
                // You can perform additional actions based on the rating
            }
        });

        sustainabilityRatings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Handle the rating change
                Toast.makeText(requireContext(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                _sustainabilityRatings = rating;
                // You can perform additional actions based on the rating
            }
        });


//        addImgBtn.setOnClickListener(view1 -> {
//            selectImage();
//        });

//        uploadImgBtn.setOnClickListener(view1 -> {
//            uploadImage();
//        });

        submitBtn.setOnClickListener(view1 -> {
            submitFormConfirmation();

        });

        View dropdownForm = view.findViewById(R.id.dropdownForm);
        View sustainabilityDetailsForm = view.findViewById(R.id.sustainabilityDetailsForm);
        sustainabilityDetailsForm.setVisibility(View.GONE);
        dropdownForm.setOnClickListener(view1 -> {
            ImageView Arrow = view1.findViewById(R.id.dropdownArrow);

            int visibility = sustainabilityDetailsForm.getVisibility();
            if (visibility == View.GONE) {
                Arrow.setImageResource(R.drawable.baseline_arrow_drop_up_24);
                sustainabilityDetailsForm.setVisibility(View.VISIBLE);
            } else {
                Arrow.setImageResource(R.drawable.baseline_arrow_drop_down_24);
                sustainabilityDetailsForm.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void submitFormConfirmation() {
        getInputValues();



        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Submission");

        String message = "BARCODE : " + _code + "\n\nDESCRIPTION : " + _description + "\n\nPRODUCT RATING : " + _ratings +
                         "\n\nPRODUCT REVIEW : " + _review + "\n\nSUSTAINABILITY RATING : " + _sustainabilityRatings +
                         "\n\nIS PRODUCT RECYCLABLE : " + productRecyclable + "\n\nRECYCLABILITY DETAILS : " + _recyclabilityDetails +
                         "\n\nENERGY EFFICIENCY DETAILS : "+_energyEfficiencyDetails+"\n\nPACKAGING DETAILS : "+_packgingDetails;
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, proceed with form submission
                submitForm();
            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing or handle as needed
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void submitForm() {
        getInputValues();

//        if (ImageList.size() > 0) {
//            uploadImage();
//        }


        String jsonBody = "{\n" +
                "  \"code\": \"" + _code + "\",\n" +
                "  \"title\": \"" + _title + "\",\n" +
                "  \"description\": \"" + _description + "\",\n" +
                "  \"ratings\":" + _ratings + ",\n" +
                "  \"review\": \"" + _review + "\",\n" +
                "  \"sustainabilityInfo\": {\n" +
                "    \"ratings\":" + _sustainabilityRatings + ",\n" +
                "    \"productRecyclability\":\"" + productRecyclable + "\",\n" +
                "    \"recyclabilityDetails\": \"" + _recyclabilityDetails + "\",\n" +
                "    \"packingDetails\": \"" + _packgingDetails + "\"\n" +
                "  }\n" +
                "}";

        // Assuming PostRequestTask is the class containing the AsyncTask
        PostRequestTask postRequestTask = new PostRequestTask(jsonBody);
        postRequestTask.execute();

        replaceFragmentWithHome();
    }


    private void getInputValues() {
        _code = barcodeInput.getText().toString();
        _title = title.getText().toString();
        _description = description.getText().toString();
        _review = review.getText().toString();
        _recyclabilityDetails = recyclabilityDetails.getText().toString();
        _energyEfficiencyDetails = energyEfficiencyDetails.getText().toString();
        _packgingDetails = packgingDetails.getText().toString();
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("UPLOADING ...");
        progressDialog.show();

        String barcode = String.valueOf(barcodeInput.getText());

        int totalUploads = ImageList.size();

        for (int i = 0; i < totalUploads; i++) {
            Log.e("FOR LOOP STARTED", "ffffssssssssss");
            Uri imageUri = ImageList.get(i);
            storageReference = FirebaseStorage.getInstance().getReference("productImage/" + barcode + "/" + i);

            storageReference.putFile(imageUri).addOnCompleteListener(task -> {
                // Always decrease the latch count after each upload

                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = String.valueOf(uri);
                        Toast.makeText(requireContext(), url, Toast.LENGTH_LONG).show();

                        imagesUrls.add(url);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            replaceFragmentWithHome();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to get download URL", Toast.LENGTH_LONG).show();
                    });
                } else {
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
                }

            });
        }
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data.getClipData() != null) {

            int count = data.getClipData().getItemCount();
            int CurrentImageSelect = 0;

//            imageUri = data.getData();
//            imageView = view.findViewById(R.id.productImage);
//            imageView.setImageURI(imageUri);


            while (CurrentImageSelect < count) {
                Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                ImageList.add(imageuri);
                CurrentImageSelect = CurrentImageSelect + 1;
                Log.e("image uri", String.valueOf(imageuri));
            }

            checkImageCount();

        }

    }

    private void makeToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean checkImageCount() {
        if (ImageList.size() > 3) {
            areImagesLimited = false;
            ImageList.clear();
            makeToast("SELECT MAXIMUM 3 IMAGES");
            selectImage();
        }

        return areImagesLimited;
    }

    private void replaceFragmentWithHome() {
        // Create a new fragment instance with the response data
        HomeFragment homeFragment = HomeFragment.newInstance();
        Log.e("frag", "RELPACEEEEEEEEEEE");

        // Get the FragmentManager and start a transaction
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragment_container, homeFragment);

        // Optional: Add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}

package com.vyndsolutions.vyndteam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vyndsolutions.vyndteam.R;
//import com.vyndsolutions.vyndteam.activities.Menu2Activity;
import com.vyndsolutions.vyndteam.activities.MenuActivity;
import com.vyndsolutions.vyndteam.models.Business;
import com.vyndsolutions.vyndteam.models.MenuProduct;
import com.vyndsolutions.vyndteam.models.MenuType;
import com.vyndsolutions.vyndteam.widgets.images.NetworkImageView;

import java.util.ArrayList;


/**
 * Created by Hoda on 27/04/2018.
 */


public class MenuBusinessAdapter extends  BaseExpandableListAdapter {
    Context context;
    ArrayList<MenuType> menuTypes;
    MenuActivity menuActivity;
    View footerView;
    private boolean isActivated = false;
    RelativeLayout rlDeleteProduct, rlEditProduct;
    NetworkImageView ivDeleteProduct, ivEditProduct;
    TextView tvProductName, tvProductPrice;
    LinearLayout layoutOneProduct;
    private MenuType menuType;
    private Business business;
    private static final int SEARCH_RESULT_LIMIT = 10;
    private final String TAG_ROOT_ELEMENT = "items";
    private int offset = 0;
    private boolean state = false;
    private int size;
    private boolean isWaitingForLoad;


    public MenuBusinessAdapter(MenuActivity menuActivity, Context context, ArrayList<MenuType> menuTypes, View footerView, boolean isActivated, Business business) {
        this.context = context;
        this.menuTypes = menuTypes;
        this.menuActivity = menuActivity;
        this.footerView = footerView;
        this.isActivated = isActivated;
        this.business = business;


    }

    public void swap() {
        switchState();
        //if (currentPage < totalPages) {
       // getBusinessMenu(business.getId());
        //}
    }

    public void switchState() {
        this.state = !this.state;
    }

    public Boolean isNotified() {
        return this.state;
    }

    @Override
    public int getGroupCount() {
        return menuTypes.size();
    }

    //Add 2 to childcount. The first row and the last row are used as header and footer to childview
    @Override
    public int getChildrenCount(int i) {
        if (isActivated) {
            try {
                return menuTypes.get(i).getProducts().size() + 1;
            } catch (NullPointerException e) {
                System.out.println("catchet 1");
                return 1;
            }

        } else {
            try {
                return menuTypes.get(i).getProducts().size();
            } catch (NullPointerException e) {
                System.out.println("catchet 2");
                //return menuTypes.size();
                return 1;
            }

        }
    }

    @Override
    public MenuType getGroup(int i) {
        return menuTypes.get(i);
    }

    @Override
    public MenuProduct getChild(int i, int i2) {
        try {
            return menuTypes.get(i).getProducts().get(i2);
        } catch (NullPointerException e) {
            return null;
        }

    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_one_categorie, null);
        }

        TextView tvCategorie = view.findViewById(R.id.tv_categorie);
        TextView tvModifier = view.findViewById(R.id.tv_categorie_update);
        NetworkImageView ivRemoveCategorie = view.findViewById(R.id.iv_remove_categorie);


        final MenuType menuType = menuTypes.get(i);

      /*  if (i == size && !isNotified()) {
            swap();
        }
        try {
            if (i == getGroupCount() - 1 && isWaitingForLoad) {
                menuActivity.progressBarItem.setVisibility(View.VISIBLE);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }*/

        if (isActivated) {
            tvModifier.setVisibility(View.VISIBLE);
            ivRemoveCategorie.setVisibility(View.VISIBLE);
            tvModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isActivated)
                        menuActivity.showUpdateCategorieDialog(menuType);
                }
            });
        } else {
            tvModifier.setVisibility(View.GONE);
            ivRemoveCategorie.setVisibility(View.GONE);
        }
        tvCategorie.setText(menuType.getLabel());
        ivRemoveCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuTypes.remove(menuType);
                notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final MenuType menuType = menuTypes.get(groupPosition);

        if (isActivated) {
            if (childPosition >= 0 && childPosition < getChildrenCount(groupPosition) - 1) {
                final MenuProduct currentChild = getChild(groupPosition, childPosition);

                view = inflater.inflate(R.layout.item_one_product, null);
                layoutOneProduct = view.findViewById(R.id.layout_product_item);
                tvProductName = view.findViewById(R.id.tv_product_name);
                tvProductPrice = view.findViewById(R.id.tv_product_price);
                ivDeleteProduct = view.findViewById(R.id.iv_delete_product);
                ivEditProduct = view.findViewById(R.id.iv_edit_product);
                rlDeleteProduct = view.findViewById(R.id.rl_edit_product);
                rlEditProduct = view.findViewById(R.id.rl_delete_product);
                try {
                    tvProductName.setText(currentChild.getLabel());
                    tvProductPrice.setText(String.valueOf(currentChild.getPrice()));

                } catch (NullPointerException e) {

                }

                rlDeleteProduct.setVisibility(View.VISIBLE);
                rlEditProduct.setVisibility(View.VISIBLE);
                ivDeleteProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuActivity.removeProductFromList(currentChild, menuType);
                    }
                });
                layoutOneProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isActivated)
                            menuActivity.showUpdateProductDialog(currentChild);
                    }
                });

            }
            if (childPosition == getChildrenCount(groupPosition) - 1) {
                view = inflater.inflate(R.layout.footer_product_list, null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        menuActivity.showAddProductDialog(menuType);
                    }
                });
            }
        } else {
            try {
                MenuProduct currentChild = getChild(groupPosition, childPosition);
                view = inflater.inflate(R.layout.item_one_product, null);
                tvProductName = view.findViewById(R.id.tv_product_name);
                tvProductPrice = view.findViewById(R.id.tv_product_price);
                ivDeleteProduct = view.findViewById(R.id.iv_delete_product);
                rlDeleteProduct = view.findViewById(R.id.rl_edit_product);
                rlEditProduct = view.findViewById(R.id.rl_delete_product);
                ivEditProduct = view.findViewById(R.id.iv_edit_product);
                tvProductName.setText(currentChild.getLabel());
                tvProductPrice.setText(String.valueOf(currentChild.getPrice()));
                rlDeleteProduct.setVisibility(View.GONE);
                rlEditProduct.setVisibility(View.GONE);

            } catch (NullPointerException e) {
                view = inflater.inflate(R.layout.footer_product_list, null);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        menuActivity.showAddProductDialog(menuType);
                    }
                });

            }


        }

        return view;
    }

    public void refresh(ArrayList<MenuType> menuTypes, boolean isActivated) {
        this.isActivated = isActivated;
        this.menuTypes = menuTypes;
        notifyDataSetChanged();

    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

   /* private void getBusinessMenu(int businessID) {
        menuActivity.progressBarItem.setVisibility(View.VISIBLE);
        isWaitingForLoad = true;

        offset += 10;


        Call<ResponseBody> call = RetrofitServiceFacotry.getBusinessApiRetrofitServiceClient()
                .getMenu(Utils.token, businessID, offset, SEARCH_RESULT_LIMIT);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (menuActivity.getActivity() != null) {
                    menuActivity.progressBarItem.setVisibility(View.GONE);
                    WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                }
                Gson gson = Utils.getGsonInstance();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    if (response.code() == 200) {
                        JSONArray jsonArrayItems = jsonObject.getJSONArray("items");
                        if (jsonArrayItems.length() == 0) {


                        } else {
                            if (jsonArrayItems.length() != 0) {

                                for (int i = 0; i < jsonArrayItems.length(); i++) {
                                    menuType = gson.fromJson(jsonArrayItems.get(i).toString(), MenuType.class);
                                    menuTypes.add(menuType);
                                }

                            }
                            if (menuTypes.size() != 0) {

                                size += jsonArrayItems.length();
                                notifyDataSetChanged();


                            }

                            if (jsonArrayItems.length() > 0) {
                                switchState();
                            }
                            WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                        }

                    } else {
                        WidgetUtils.enableUserInteraction(menuActivity.getActivity());

                    }
                    isWaitingForLoad = false;
                } catch (JSONException | IOException | NullPointerException e) {
                    WidgetUtils.enableUserInteraction(menuActivity.getActivity());
                    isWaitingForLoad = false;


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //WidgetUtils.hideProgressDialog(mProgressDialog,getActivity());
                isWaitingForLoad = false;
                if (menuActivity.getActivity() != null) {

                    menuActivity.progressBarItem.setVisibility(View.GONE);
                }

            }
        });
    }*/
}

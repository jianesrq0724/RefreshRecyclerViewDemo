package com.ruiqin.refreshdemo.module.trading.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiqin.refreshdemo.R;
import com.ruiqin.refreshdemo.module.trading.entiy.RespTradingRecord;
import com.ruiqin.refreshdemo.util.ToastUtils;

import java.util.List;


/**
 * Created by ruiqin.shen
 * 类说明：收支明细 适配器
 */
public class TradingRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RespTradingRecord.RecordsBean> mTradingRecordList;
    private Context mContext;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //加载结束
    public static final int LOADING_END = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status = PULLUP_LOAD_MORE;

    public TradingRecordAdapter(List<RespTradingRecord.RecordsBean> tradingRecordList) {
        mTradingRecordList = tradingRecordList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {//无记录的时候
            mContext = parent.getContext();
            return new NoTradingRecordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_trading_record_no, parent, false));
        } else if (viewType == 1) {//有记录的时候，包含时间
            mContext = parent.getContext();
            TradingRecordViewHolder tradingRecordViewHolder = new TradingRecordViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_trading_record, parent, false));
            tradingRecordViewHolder.mLlTradingDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort("haha");
                }
            });
            return tradingRecordViewHolder;
        } else if (viewType == 10) {//加载更多
            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false));
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TradingRecordViewHolder) {//收支明细，有日期

            //判断是否隐藏日期并设置
            judgeDateShowAndHide(holder, position);

            ((TradingRecordViewHolder) holder).mItemStatus.setText(mTradingRecordList.get(position).getStatus());

            //收入支出是不同的样式，收入支付类型（1收入0支出）
            if (mTradingRecordList.get(position).getType() == 0) {//支出
                ((TradingRecordViewHolder) holder).mItemAmount.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                ((TradingRecordViewHolder) holder).mItemAmount.setText("" + mTradingRecordList.get(position).getAmount());
            } else {//收入
                ((TradingRecordViewHolder) holder).mItemAmount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                ((TradingRecordViewHolder) holder).mItemAmount.setText("" + mTradingRecordList.get(position).getAmount());
            }
        } else if (holder instanceof FootViewHolder) {//底部加载提示
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.mItemLoadHint.setVisibility(View.VISIBLE);//上拉加载 文字提示
                    if (weatherHide) {
                        footViewHolder.view.setVisibility(View.GONE);
                    }
                    break;
                case LOADING_MORE:
                    footViewHolder.mItemLoadHint.setVisibility(View.GONE);//加载更多中
                    break;
                case LOADING_END:
                    footViewHolder.mItemLoadHint.setVisibility(View.VISIBLE);//
                    footViewHolder.mItemLoadHint.setText("我是有底线的");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 判断日期显示还是隐藏
     */
    private void judgeDateShowAndHide(RecyclerView.ViewHolder holder, int position) {
        String currentTime = mTradingRecordList.get(position).getTime();
        if (position == 0) {//只有一条记录，展示日期
            ((TradingRecordViewHolder) holder).mItemLayoutDate.setVisibility(View.VISIBLE);
            ((TradingRecordViewHolder) holder).mItemData.setText(currentTime);
        } else {
            String lastTime = mTradingRecordList.get(position - 1).getTime();
            if (currentTime.equals(lastTime)) {//相同隐藏日期
                ((TradingRecordViewHolder) holder).mItemLayoutDate.setVisibility(View.GONE);
            } else {//不相同显示日期，并设置值
                ((TradingRecordViewHolder) holder).mItemLayoutDate.setVisibility(View.VISIBLE);
                ((TradingRecordViewHolder) holder).mItemData.setText(currentTime);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTradingRecordList.size() == 0 ? 1 : mTradingRecordList.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (mTradingRecordList.size() == 0) {//0表示无记录
            return 0;
        } else if (position + 1 == getItemCount()) {//底部加载进度条
            return 10;
        } else {//有数据的时候，返回1
            return 1;
        }
    }


    /**
     * 交易详情数据，有日期
     */
    public static class TradingRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemData;
        private TextView mItemAmount;
        private TextView mItemStatus;
        private LinearLayout mLlTradingDetail;
        private LinearLayout mItemLayoutDate;

        public TradingRecordViewHolder(View itemView) {
            super(itemView);
            mItemLayoutDate = (LinearLayout) itemView.findViewById(R.id.layout_date);
            mItemData = (TextView) itemView.findViewById(R.id.item_data);
            mLlTradingDetail = (LinearLayout) itemView.findViewById(R.id.ll_trading_detail);
            mItemAmount = (TextView) itemView.findViewById(R.id.item_amount);
            mItemStatus = (TextView) itemView.findViewById(R.id.item_status);
        }
    }

    /**
     * 加载更多进度条
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemLoadHint;
        View view;

        public FootViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mItemLoadHint = (TextView) itemView.findViewById(R.id.item_load_hint);
        }
    }


    /**
     * 无收支记录
     */
    public static class NoTradingRecordViewHolder extends RecyclerView.ViewHolder {
        public NoTradingRecordViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    private boolean weatherHide = false;

    public void setHideLoadMoreHint() {
        weatherHide = true;
    }

}

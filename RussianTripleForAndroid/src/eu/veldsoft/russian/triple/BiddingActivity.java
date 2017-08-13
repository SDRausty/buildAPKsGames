package eu.veldsoft.russian.triple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import eu.veldsoft.russian.triple.model.Bid;
import eu.veldsoft.russian.triple.model.Bidding;

/**
 * Bidding screen.
 * 
 * @author Todor Balabanov
 */
public class BiddingActivity extends Activity {
	/**
	 * Current bid key.
	 */
	static final String EXTRA_BIDDING_KEY = "eu.veldsoft.russian.triple.biddingKey";

	/**
	 * Bid result key.
	 */
	static final String EXTRA_RESULT_BID_KEY = "eu.veldsoft.russian.triple.resultBidKey";

	/**
	 * Bid pass key.
	 */
	static final String EXTRA_PASS_BID_KEY = "eu.veldsoft.russian.triple.passBidKey";

	/**
	 * Bid for pass situation.
	 */
	static final int PASS_VALUE = 0;

	/**
	 * Reference to the bidding object.
	 */
	private Bidding bidding = null;

	/**
	 * Last bid value.
	 */
	private int last = 0;

	/**
	 * Maximum valid bid.
	 */
	private int maximum = 0;

	/**
	 * Current bid value.
	 */
	private int current = 0;

	/**
	 * Current bid setter.
	 * 
	 * @param current
	 *            Value of the current bid.
	 */
	private void updateViews(int current) {
		if (bidding == null) {
			return;
		}

		/*
		 * Bid value can not be less than minimum valid bid value according game
		 * rules.
		 */
		if (current < Bid.MIN_VALID_BID_VALUE) {
			return;
		}

		/*
		 * Current bid can not be more than maximum possible.
		 */
		if (current > maximum) {
			return;
		}

		this.current = current;

		((TextView) findViewById(R.id.currentBidderName)).setText(""
				+ bidding.getCurrentBidder().getName());
		((TextView) findViewById(R.id.maxValidBid)).setText("" + maximum);
		((TextView) findViewById(R.id.bidValue)).setText("" + last);

		Spinner spinner = (Spinner) findViewById(R.id.bidding);
		for (int i = 0; i < spinner.getCount(); i++) {
			Object value = spinner.getItemAtPosition(i);
			if (("" + current).equals(value)) {
				spinner.setSelection(i);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStart() {
		super.onStart();

		bidding = (Bidding) getIntent().getSerializableExtra(EXTRA_BIDDING_KEY);

		last = current = bidding.hasLast() == true ? bidding.last().getScore()
				: 0;
		maximum = (new Bid(last, bidding.getCurrentBidder())).maximum();

		updateViews(current);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bidding);

		((Button) findViewById(R.id.pass))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(Activity.RESULT_OK, (new Intent().putExtra(
								EXTRA_RESULT_BID_KEY, PASS_VALUE).putExtra(
								EXTRA_PASS_BID_KEY, true)));
						finish();
					}
				});

		((Button) findViewById(R.id.plus_one))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						updateViews(current + 1);
					}
				});

		((Button) findViewById(R.id.plus_ten))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						updateViews(current + 10);
					}
				});

		((Button) findViewById(R.id.done))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						/**
						 * Bidding can not finish without rise of the bid.
						 */
						if (last == current) {
							return;
						}

						setResult(Activity.RESULT_OK, (new Intent().putExtra(
								EXTRA_RESULT_BID_KEY, current).putExtra(
								EXTRA_PASS_BID_KEY, false)));
						finish();
					}
				});

		((Spinner) findViewById(R.id.bidding))
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long arg3) {
						/*
						 * First value is not a number.
						 */
						if (position == 0) {
							return;
						}

						int value = new Integer(parent.getItemAtPosition(
								position).toString()).intValue();
						if (value <= current || value > maximum) {
							updateViews(current);
						} else {
							updateViews(value);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}
}

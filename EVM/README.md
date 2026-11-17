# Module 8 — EVM Calculator

A free, standalone earned value management (EVM) calculator for teaching and learning. Runs entirely in the browser; no server, dependencies, accounts, API keys or build step are required.

## Publish on GitHub Pages

1. Create a GitHub repository (for example, `module8-evm-calculator`).
2. Upload **`index.html`** to the repository root (you may also include this README).
3. In the repository, open **Settings → Pages**.
4. Under **Build and deployment**, choose **Deploy from a branch**, then select **main** and **/(root)** and save.
5. Open the GitHub Pages URL shown in the settings, typically `https://YOUR-USERNAME.github.io/module8-evm-calculator/`.

## How to use

1. Give the project a name and select its EVM status date.
2. Add tasks **one by one**, entering their **total budget (BAC)**, **planned % complete**, **actual physical % complete**, and **actual cost (AC)**.
3. Inspect each task's automatically calculated **PV, EV, CV, SV, CV%, SV%, CPI, SPI** and the **TOTAL** row.
4. Edit task inputs directly in the table, remove tasks, or add more tasks at any time.
5. Use **Export CSV** to obtain a spreadsheet-compatible report, or **Backup JSON / Import JSON** to move the project between browsers. The project is also saved locally in your browser.
6. Review the four charts: project value comparison, cost variance by task, schedule variance by task, and planned-versus-earned work. Choose a forecast scenario to display EAC, ETC, VAC, TCPI and their interpretations.
7. Optionally expand the **PERT estimator** to calculate three-point duration and cost estimates. The expected PERT cost may be placed in the new-task budget field using the button.

## EVM formulas

- `PV = Σ (task BAC × planned completion fraction)`
- `EV = Σ (task BAC × actual physical completion fraction)`
- `AC = Σ actual costs`
- `CV = EV − AC`; `SV = EV − PV`
- `CV% = (EV − AC) / EV × 100`; `SV% = (EV − PV) / PV × 100`
- `CPI = EV / AC`; `SPI = EV / PV`
- `BAC = Σ task budgets`
- PERT expected estimate: `(optimistic + 4 × most likely + pessimistic) / 6`
- PERT standard deviation: `(pessimistic − optimistic) / 6`

CPI/SPI and variance percentages show a dash if the corresponding denominator is zero. Schedule variance is expressed in **budget dollars**, not weeks.

## Forecasting methods

The dropdown lets the user explicitly choose which assumption to use:

- **Current CPI continues:** `EAC = BAC / CPI`
- **Remaining work at budgeted efficiency:** `EAC = AC + (BAC − EV)`
- **CPI and SPI pressure continue (scenario):** `EAC = AC + (BAC − EV) / (CPI × SPI)`
- **Management estimate:** `EAC = AC + manually entered ETC`

Additional formulas: `ETC = EAC − AC`, `VAC = BAC − EAC`, `TCPI(BAC) = (BAC − EV) / (BAC − AC)` and `TCPI(EAC) = (BAC − EV) / (EAC − AC)`. Forecast outputs are omitted when required denominators are zero or invalid. These **cost estimates** do not predict a schedule finish date.

The four charts reflect only the latest entered task snapshot, **not a fabricated historical monthly S-curve**. Recording actual history would require monthly dated baseline and earned value data.

## Sample data

The first launch opens with the four fictional TMU Church & McGill Student Residence work packages used in the Month 8 teaching example. The sample has `BAC = $23,700,000`, `PV = $11,100,000`, `EV = $8,025,000`, `AC = $9,400,000`, `CV = -$1,375,000`, `SV = -$3,075,000`, `CPI ≈ 0.854`, and `SPI ≈ 0.723`. Click **Clear tasks** to build a project from scratch; click **Load TMU example** to restore the dataset.

## Privacy

All input data stays in the user's browser (localStorage), unless they explicitly export it. Clearing browser storage will erase the locally saved project; use **Backup JSON** for portability.

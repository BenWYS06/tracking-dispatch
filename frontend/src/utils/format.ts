const priceFormatter = new Intl.NumberFormat("en-US", {
  minimumFractionDigits: 0,
  maximumFractionDigits: 2,
  useGrouping: false,
});

/**
 * Format an amount in hryvnia, e.g. `389.9` -> `"389.9₴"`.
 * Rounds to 2 decimals so summed cart totals don't show float noise
 * like `389.90000000000003`.
 */
export function formatPrice(amount: number): string {
  return `${priceFormatter.format(amount)}₴`;
}

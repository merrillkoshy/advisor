export function groupBy<T, K extends keyof T>(
  items: T[],
  key: K,
): Record<string, Omit<T, K>[]> {
  return items.reduce<Record<string, Omit<T, K>[]>>((acc, item) => {
    const groupValue = item[key];
    const groupKey =
      typeof groupValue === "symbol" ? String(groupValue) : String(groupValue);

    if (!acc[groupKey]) {
      acc[groupKey] = [];
    }

    const { [key]: _, ...itemWithoutKey } = item;
    acc[groupKey].push(itemWithoutKey as Omit<T, K>);
    return acc;
  }, {});
}

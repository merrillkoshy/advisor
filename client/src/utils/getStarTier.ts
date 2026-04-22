export const getStarTier = (level: number) => {
  if (level < 30) return 0;
  if (level < 50) return 1;
  if (level < 70) return 2;
  if (level < 90) return 3;
  if (level < 110) return 4;
  return 5;
};

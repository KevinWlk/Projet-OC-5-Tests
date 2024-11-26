export function getMonthName (monthNumber: number) : String {
  const date = new Date();
  date.setMonth(monthNumber-1);
  return date.toLocaleString("en-US", { month : "long"});
}

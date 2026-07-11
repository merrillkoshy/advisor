import {
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
  Table as UiTable,
} from "@/components/ui/table";
import { isObject } from "@/utils/isObject";
import type { TableHTMLAttributes } from "react";

interface SimpleTableProps extends TableHTMLAttributes<HTMLTableElement> {
  data: Record<string, Record<string, any>[]>;
  caption?: string;
  removeKeys?: string[];
}

export function Table({
  data,
  caption,
  removeKeys,
  ...props
}: SimpleTableProps) {
  if (!isObject(data)) return;
  const headers = Object?.keys(data)?.filter(
    (item) => !removeKeys?.includes(item),
  );

  const values = Object?.values(data);

  return (
    <UiTable className="w-full" {...props}>
      {caption ? <TableCaption>{caption}</TableCaption> : null}
      <TableHeader>
        <TableRow>
          {headers.map((header) => (
            <TableHead key={header} className="w-full">
              {header}
            </TableHead>
          ))}
        </TableRow>
      </TableHeader>
      <TableBody>
        <TableRow>
          {headers.map((item) => (
            <TableCell>{values[item]}</TableCell>
          ))}

          {/* {values.map((items, index) => (
          <TableRow key={index}>
            {items?.map((item, idx) => {
              const subHeaders = Object.keys(item);
              const subValues = Object.values(item);
              return (
                <TableCell key={idx} className="font-medium">
                  <TableHeader>
                    <TableRow>
                      {subHeaders?.map((header) => (
                        <TableHead key={header} className="w-full">
                          {header}
                        </TableHead>
                      ))}
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {subValues?.map((value) => (
                      <TableCell>
                        <p className="text-wrap">
                          {JSON.stringify(value, null, 2)}
                        </p>
                      </TableCell>
                    ))}
                  </TableBody>
                </TableCell>
              );
            })} */}
        </TableRow>
      </TableBody>
    </UiTable>
  );
}

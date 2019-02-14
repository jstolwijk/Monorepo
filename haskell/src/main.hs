import System.IO.Unsafe
import Text.Read

-- main = print part1

main = print(part2) 

part1 = sum (unsafePerformIO input)
part2 = findFrequency [] (cycle (unsafePerformIO input)) 0

findFrequency :: [Int] -> [Int] -> Int -> Int
findFrequency seen (i:is) freq = case (elem newFreq seen) of 
              False -> findFrequency (newFreq:seen) is newFreq
              True -> newFreq
              where newFreq = i + freq

parseString :: String -> Int
parseString ('-': num) = -(parseNumber num)
parseString ('+': num) = parseNumber num

parseNumber :: String -> Int 
parseNumber num = case (readMaybe num) of
                  (Just n) -> n
                  Nothing -> 0

input :: IO [Int]
input = do 
        fileString <- readFile "input1.txt"
        let content = lines fileString
        let parsedContent = map (parseString . takeWhile ('\r' /=)) content
        return parsedContent

-- fac 0 = 1
-- fac n = n * fac (n-1)
 
-- main = print (fac 42)


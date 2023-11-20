-- KEYS[1] => key

local tbl = {}

local result = redis.call("HGETALL", KEYS[1])
if #result == 0 then
    return tbl
end

for i =1, #result, 2 do
    if string.match(result[i], "^[ADE]") then
        tbl[#tbl+1] = result[i]
        local value = result[i + 1]
        tbl[#tbl+1] = string.upper(value)
    end
end

return tbl
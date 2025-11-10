local current = redis.call("incr", KEYS[1])
if tonumber(current) == 1 then
  redis.call("expire", KEYS[1], tonumber(ARGV[2]))
end
if tonumber(current) <= tonumber(ARGV[1]) then
  return {1, current}
else
  return {0, current}
end
